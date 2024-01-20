package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


class SqliteClient(db: Db) : Client(db) {
	override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select 'main' as name
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT 'main'       as _schema,
					tbl_name    as name
			FROM sqlite_master WHERE type='table'
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) {
		val sql = tables.map {
			"""
			SELECT 'main'               as _schema,
				    '${it.value.name}'  as _table,
				    name                as name,
				    NOT [notnull]       as "isNull",
				    type                as type,
				    pk                  as primaryKey
			 FROM PRAGMA_TABLE_INFO('${it.value.name}')
			""".trimIndent().replace("\n", "")
		}.joinToString(" UNION ALL ")

		return this.exec(sql = sql, onResultSet = onResultSet)
	}

	override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) {
		val sql = tables.map {
			"""
			SELECT 'main'             as fromSchema,
				   '${it.value.name}' AS fromTable,
				   "from"             AS fromColumn,
				   'main'             AS toSchema,
				   "table"            AS toTable,
				   "to"               AS toColumn
			 FROM PRAGMA_FOREIGN_KEY_LIST('${it.value.name}')
			""".trimIndent().replace("\n", "")
		}.joinToString(" UNION ALL ")

		return this.exec(sql = sql, onResultSet = onResultSet)
	}

}
