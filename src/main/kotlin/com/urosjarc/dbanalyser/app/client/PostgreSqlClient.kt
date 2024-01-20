package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


open class PostgreSqlClient(db: Db) : Client(db) {
	override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select current_database() || '.' || SCHEMA_NAME as name
			from information_schema.schemata
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select
				current_database() || '.' || tab.table_schema as _schema,
				tab.table_name   as name
			from information_schema.tables tab
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT current_database() || '.' || c.table_schema                   as _schema,
				   c.TABLE_NAME                                                  as _table,
				   c.COLUMN_NAME                                                 as name,
				   c.DATA_TYPE                                                   as type,
				   c.is_nullable                                                 as "isNull",
				   CASE WHEN pk.column_name is not null THEN true ELSE false END AS primaryKey
			FROM INFORMATION_SCHEMA.COLUMNS c
					 LEFT JOIN (SELECT ku.TABLE_CATALOG, ku.TABLE_SCHEMA, ku.TABLE_NAME, ku.COLUMN_NAME
								FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS tc
										 INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS ku
													ON tc.CONSTRAINT_TYPE = 'PRIMARY KEY'
														AND tc.CONSTRAINT_NAME = ku.CONSTRAINT_NAME) pk
							   ON c.TABLE_CATALOG = pk.TABLE_CATALOG
								   AND c.TABLE_SCHEMA = pk.TABLE_SCHEMA
								   AND c.TABLE_NAME = pk.TABLE_NAME
								   AND c.COLUMN_NAME = pk.COLUMN_NAME
		""".trimIndent(), onResultSet = onResultSet
	)

	override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT
				current_database() || '.' || tc.table_schema    as fromSchema,
				tc.table_name                                   as fromTable,
				kcu.column_name                                 as fromColumn,
				current_database() || '.' || ccu.table_schema   AS toSchema,
				ccu.table_name                                  AS toTable,
				ccu.column_name                                 AS toColumn
			FROM information_schema.table_constraints           AS tc
				 JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema
				 JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name
			WHERE tc.constraint_type = 'FOREIGN KEY'
        """.trimIndent(), onResultSet = onResultSet
	)

}
