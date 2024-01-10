package com.urosjarc.dbanalyser.app.client

import com.jakewharton.fliptables.FlipTableConverters
import com.urosjarc.dbanalyser.app.table.TableConnection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.ResultSet

class ClientService : KoinComponent {
	val clientRepo by this.inject<ClientRepo>()

	fun joinSql(endTableConnection: TableConnection, countRelations: Boolean): String {
		var node = endTableConnection
		val takenSigns = mutableSetOf<String>()

		val joinsSql = mutableListOf<String>()
		var columnsSql = mutableListOf<String>()
		while (node.parent != null) {

			val fkey = node.foreignKey
			val start = if (node.isParent) fkey?.from else fkey?.to
			val end = if (node.isParent) fkey?.to else fkey?.from

			val startTable = start?.table
			val startSign = startTable?.uniqueShortName(taken = takenSigns)!!
			val startColumn = start.name

			val endTable = end?.table
			val endSign = endTable?.uniqueShortName(taken = takenSigns)!!
			val endColumn = end.name

			takenSigns.add(startSign)

			joinsSql.add(0, """JOIN ${startTable} "${startSign}" ON "${startSign}".${startColumn} = "${endSign}".${endColumn}""")
			startTable.columns.forEach { columnsSql.add(""""$startSign".${it.name}""") }

			node = node.parent!!
		}

		val startTableShortName = node.table.uniqueShortName(taken = takenSigns)
		takenSigns.add(startTableShortName)
		columnsSql.addAll(0, node.table.columns.map { """"${startTableShortName}".${it.name}""" })

		val primaryKey = """"${startTableShortName}".${node.table.primaryKey?.name}"""

		if (countRelations) {
			columnsSql = mutableListOf("COUNT(1) as count")
			joinsSql.add("GROUP BY $primaryKey")
		}

		return """
			SELECT
				${columnsSql.joinToString(",\n" + "\t".repeat(4))}
			FROM ${node.table} "$startTableShortName"
				${joinsSql.joinToString("\n" + "\t".repeat(4))}
		""".trimIndent()
	}

	fun countMaxRelations(endTableConnection: TableConnection): Int {
		val countRelationsSql = this.joinSql(endTableConnection = endTableConnection, countRelations = true)
		val sql = """
			SELECT MAX(X.count)
			FROM ($countRelationsSql) X
		""".trimIndent()
		return this.clientRepo.chosen!!.execOne(sql) { it.getInt(1) } ?: -1
	}

	fun execute(sql: String): String {
		val comments = sql.split("\n")
			.filter { it.startsWith("--") }
			.map { it.replace("--", "") }
			.toMutableList()

		var text = ""

		this.clientRepo.chosen?.execMany(sql=sql) {
			val table = FlipTableConverters.fromResultSet(it)
			val comment = comments.removeFirstOrNull()
			text += "$comment\n$table\n\n"
		}

		return text
	}
}
