package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


class OracleClient(db: Db) : Client(db) {
    override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            SELECT username as "name" FROM user_users
        """.trimIndent(), onResultSet = onResultSet
    )

    override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            SELECT TABLESPACE_NAME as "_schema",
                   TABLE_NAME      as name
            FROM user_tables
            where TABLESPACE_NAME is not null and TABLESPACE_NAME not in ('SYSAUX')
        """.trimIndent(), onResultSet = onResultSet
    )

    override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
			select t.TABLESPACE_NAME                 as "_schema",
				   col.TABLE_NAME                    as "_table",
				   col.COLUMN_NAME                   as "name",
				   col.DATA_TYPE                     as "type",
				   col.NULLABLE                      as "isNull",
				   SUBSTR(col.IDENTITY_COLUMN, 1, 1) as "primaryKey"
			from sys.USER_TAB_COLS col
					 inner join sys.user_tables t on col.TABLE_NAME = t.TABLE_NAME
            where t.TABLESPACE_NAME is not null and TABLESPACE_NAME not in ('SYSAUX')
		""".trimIndent(), onResultSet = onResultSet
    )

    override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            SELECT
                a.OWNER as "fromSchema",
                a.table_name as "fromTable",
                a.column_name as "fromColumn",

                c.owner as "toSchema",
                c_pk.table_name as "toTable",
                b.column_name as "toColumn"
            FROM user_cons_columns a
                     JOIN user_constraints c ON a.owner = c.owner
                AND a.constraint_name = c.constraint_name
                     JOIN user_constraints c_pk ON c.r_owner = c_pk.owner
                AND c.r_constraint_name = c_pk.constraint_name
                     JOIN user_cons_columns b ON C_PK.owner = b.owner
                AND  C_PK.CONSTRAINT_NAME = b.constraint_name AND b.POSITION = a.POSITION
            where a.OWNER is not null and a.OWNER not in ('SYSAUX')
        """.trimIndent(), onResultSet = onResultSet
    )

}
