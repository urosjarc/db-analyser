package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.ResultSet


open class DerbyClient(db: Db) : Client(db) {
    override fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            select SCHEMANAME as name from sys.SYSSCHEMAS
        """.trimIndent(), onResultSet = onResultSet
    )

    override fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            select
                s.SCHEMANAME as "_schema",
                t.TABLENAME as name
                from sys.SYSTABLES t
            join SYS.SYSSCHEMAS S on t.SCHEMAID = S.SCHEMAID
        """.trimIndent(), onResultSet = onResultSet
    )

    override fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            select
                S.SCHEMANAME as "_schema",
                t.TABLENAME as "_table",
                c.COLUMNNAME as name,
                c.COLUMNDATATYPE as type,
                0 as isNull,
                0 as primaryKey
            from sys.SYSCOLUMNS c
             INNER JOIN SYS.SYSTABLES t ON c.REFERENCEID = t.TABLEID
            inner join SYS.SYSSCHEMAS S on t.SCHEMAID = S.SCHEMAID
		""".trimIndent(), onResultSet = onResultSet
    )

    override fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit) = this.exec(
        """
            select 
                FS.SCHEMANAME as "fromScheme",
                FT.TABLENAME  as "fromTable",
                TS.SCHEMANAME as "toScheme",
                TT.TABLENAME  as "toTable"
            from sys.SYSFOREIGNKEYS FK
                join SYS.SYSCONSTRAINTS S0 on FK.CONSTRAINTID = S0.CONSTRAINTID
                join SYS.SYSCONSTRAINTS S1 on FK.KEYCONSTRAINTID = S1.CONSTRAINTID
                join sys.SYSTABLES FT on S0.TABLEID = FT.TABLEID
                join sys.SYSSCHEMAS FS on FS.SCHEMAID = S0.SCHEMAID
                join sys.SYSTABLES TT on S1.TABLEID = TT.TABLEID
                join sys.SYSSCHEMAS TS on TS.SCHEMAID = S1.SCHEMAID;
        """.trimIndent(), onResultSet = onResultSet
    )

}
