package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


open class H2Client(db: Db) : Client(db) {
    override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            select
                SCHEMA_NAME as name
            from INFORMATION_SCHEMA.SCHEMATA
        """.trimIndent(), onResultSet = onResultSet
    )

    override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            select
                   table_schema as _schema,
                   table_name as name
            from INFORMATION_SCHEMA.TABLES
        """.trimIndent(), onResultSet = onResultSet
    )

    override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            select
                   TABLE_SCHEMA as _schema,
                   TABLE_NAME as _table,
                   COLUMN_NAME as name,
                   IS_NULLABLE as isNull,
                   DATA_TYPE as type,
                   CASE WHEN IS_IDENTITY = 'YES' THEN true ELSE false END  as primaryKey
            from INFORMATION_SCHEMA.COLUMNS
		""".trimIndent(), onResultSet = onResultSet
    )

    override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
        """.trimIndent(), onResultSet = onResultSet
    )

}
