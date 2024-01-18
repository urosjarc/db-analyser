package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.query.QueryResult
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.tableConnection.TableConnection
import com.urosjarc.dbanalyser.shared.FlipTableConverters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.ResultSet
import java.sql.ResultSetMetaData

class ClientService : KoinComponent {
	val clientRepo by this.inject<ClientRepo>()

	fun countMaxRelations(endTableConnection: TableConnection): Int {
		val countRelationsSql = this.selectSql(endTableConnection = endTableConnection, countRelations = true)
		val sql = """
			SELECT MAX(X.count)
			FROM ($countRelationsSql) X
		""".trimIndent()
		return this.clientRepo.chosen!!.execOne(sql) { it.getInt(1) } ?: -1
	}

	fun execute(sql: String): MutableList<QueryResult> {
		val comments = sql.split("\n")
			.filter { it.startsWith("--") }
			.map { it.replace("--", "") }
			.toMutableList()

		var queries = mutableListOf<QueryResult>()

		this.clientRepo.chosen?.execMany(sql = sql) {
			val table = FlipTableConverters.fromResultSet(it)
			val comment = comments.removeFirstOrNull()
			queries.add(
				QueryResult(
					title = (comment ?: "DTO").replace(" ", ""),
					comment = comment ?: "DTO",
					data = "$comment\n$table\n\n",
					headers = this.getHeaders(resultSet = it),
				)
			)
		}

		return queries
	}

	fun getHeaders(resultSet: ResultSet): MutableList<QueryResult.Header> {
		val headers = mutableListOf<QueryResult.Header>()
		val metadata: ResultSetMetaData = resultSet.metaData
		val columnCount: Int = metadata.columnCount
		for (i in 1..columnCount) {
			headers.add(
				QueryResult.Header(
					name = metadata.getColumnName(i),
					type = metadata.getColumnTypeName(i),
				)
			)
		}
		return headers
	}

	fun selectSql(endTableConnection: TableConnection, countRelations: Boolean): String {
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
			-- SELECT: ${node.table}
			
			SELECT
				${columnsSql.joinToString(",\n" + "\t".repeat(4))}
			FROM ${node.table} "$startTableShortName"
				${joinsSql.joinToString("\n" + "\t".repeat(4))}
		""".trimIndent()
	}

	fun insertSql(table: Table): String {
		val columnsSql = table.columns.map { it.name }
		val columns = columnsSql.joinToString(", ")

		return """
			-- INSERT: $table
			
			INSERT INTO $table
				($columns)
			VALUES
				($columns);
		""".trimIndent()
	}
}
