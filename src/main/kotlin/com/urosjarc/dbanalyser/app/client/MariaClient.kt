package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


open class MariaClient(db: Db) : Client(db) {
	override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select SCHEMA_NAME as name
			from information_schema.schemata
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select
				   tab.table_schema as _schema,
				   tab.table_name   as name,
				   tab.create_time  as created,
				   tab.update_time  as modified
			from information_schema.tables tab
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT
				TABLE_SCHEMA as _schema,
				TABLE_NAME as _table,
				COLUMN_NAME as name,
				COLUMN_TYPE as type,
				IF(COLUMN_KEY = 'PRI', true, false) as primaryKey,
				col.is_nullable as isNull
			FROM information_schema.columns col
		""".trimIndent(), onResultSet = onResultSet
	)

	override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT
				TABLE_SCHEMA as fromSchema,
				TABLE_NAME as fromTable,
				COLUMN_NAME as fromColumn,
				REFERENCED_TABLE_SCHEMA as toSchema,
				REFERENCED_TABLE_NAME as toTable,
				REFERENCED_COLUMN_NAME as toColumn
			FROM information_schema.key_column_usage col
			where REFERENCED_COLUMN_NAME is not null
			  and REFERENCED_TABLE_NAME is not null
			  and REFERENCED_TABLE_SCHEMA is not null
        """.trimIndent(), onResultSet = onResultSet
	)

}
