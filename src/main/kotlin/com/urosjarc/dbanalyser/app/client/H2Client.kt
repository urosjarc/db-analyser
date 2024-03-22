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
            SELECT
                   UF.TABLE_SCHEMA as fromSchema,
                   UF.TABLE_NAME as fromTable,
                   UF.COLUMN_NAME as fromColumn,
                   UU.TABLE_SCHEMA as toSchema,
                   UU.TABLE_NAME as toTable,
                   UU.COLUMN_NAME as toColumn
            FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R
                     JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE UF
                          ON (R.CONSTRAINT_CATALOG, R.CONSTRAINT_SCHEMA, R.CONSTRAINT_NAME)
                              = (UF.CONSTRAINT_CATALOG, UF.CONSTRAINT_SCHEMA, UF.CONSTRAINT_NAME)
                     JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE UU
                          ON (R.UNIQUE_CONSTRAINT_CATALOG, R.UNIQUE_CONSTRAINT_SCHEMA, R.UNIQUE_CONSTRAINT_NAME)
                                 = (UU.CONSTRAINT_CATALOG, UU.CONSTRAINT_SCHEMA, UU.CONSTRAINT_NAME)
                              AND UU.ORDINAL_POSITION = UF.POSITION_IN_UNIQUE_CONSTRAINT;
        """.trimIndent(), onResultSet = onResultSet
    )

}
