package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


open class Db2Client(db: Db) : Client(db) {
	override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select
				TRIM(schemaname) as "name"
			from syscat.schemata
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select
				TRIM(tab.TABSCHEMA) as "_schema",
				TRIM(tab.TABNAME) as "name",
				TRIM(tab.CREATE_TIME) as "created",
				TRIM(tab.ALTER_TIME) as "modified"
			from syscat.tables as tab
			where tab.TYPE = 'T' and tab.OWNERTYPE = 'U' and trim(tab.TABSCHEMA) <> 'SYSTOOLS';
        """.trimIndent(), onResultSet = onResultSet
	)

	override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select
				TRIM(C.TABSCHEMA) as "_schema",
				TRIM(C.TABNAME) as "_table",
				TRIM(C.COLNAME) as "name",
				TRIM(C.TYPENAME) as "type",
				case when C.identity ='Y' then 1 else 0 end as "primaryKey",
				case when C.nulls='Y' then 1 else 0 end as "isNull"
			from syscat.columns C
					 inner join SYSCAT.TABLES T on C.TABNAME = T.TABNAME
			where T.OWNERTYPE = 'U' and T.TABSCHEMA <> 'SYSTOOLS'
		""".trimIndent(), onResultSet = onResultSet
	)

	override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select
				TRIM(ref.tabschema) as "fromSchema",
				TRIM(ref.tabname) as "fromTable",
				TRIM(ref.FK_COLNAMES) as "fromColumn",
				TRIM(ref.reftabschema) as "toSchema",
				TRIM(ref.reftabname) as "toTable",
				TRIM(ref.PK_COLNAMES) as "toColumn"
			from syscat.references ref;
        """.trimIndent(), onResultSet = onResultSet
	)}
