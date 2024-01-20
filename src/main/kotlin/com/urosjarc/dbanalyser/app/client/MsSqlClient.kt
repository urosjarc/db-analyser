package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


open class MsSqlClient(db: Db) : Client(db) {
	override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select sch.name as name
			from sys.schemas sch
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select sch.name        as _schema,
				   tab.name        as name,
				   tab.create_date as created,
				   tab.modify_date as modified
			from sys.tables tab
					 INNER JOIN sys.schemas sch ON sch.schema_id = tab.schema_id
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT sch.name        as _schema,
				   tab.name        as _table,
				   col.name        as name,
				   col.is_nullable as isNull,
				   typ.name        as type,
				   col.is_identity as primaryKey
			FROM sys.columns col
					 INNER JOIN sys.tables tab on col.object_id = tab.object_id
					 INNER JOIN sys.schemas sch ON sch.schema_id = tab.schema_id
					 inner JOIN sys.types typ ON col.user_type_id = typ.user_type_id
		""".trimIndent(), onResultSet = onResultSet
	)

	override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT sch1.name as fromSchema,
				   tab1.name AS fromTable,
				   col1.name AS fromColumn,
				   sch2.name AS toSchema,
				   tab2.name AS toTable,
				   col2.name AS toColumn
			FROM sys.foreign_key_columns fkc
					 INNER JOIN sys.tables tab1 ON tab1.object_id = fkc.parent_object_id
					 INNER JOIN sys.tables tab2 ON tab2.object_id = fkc.referenced_object_id
					 INNER JOIN sys.schemas sch1 ON tab1.schema_id = sch1.schema_id
					 INNER JOIN sys.schemas sch2 ON tab2.schema_id = sch2.schema_id
					 INNER JOIN sys.columns col1 ON col1.column_id = parent_column_id AND col1.object_id = tab1.object_id
					 INNER JOIN sys.columns col2 ON col2.column_id = referenced_column_id
				AND col2.object_id = tab2.object_id;
        """.trimIndent(), onResultSet = onResultSet
	)

}
