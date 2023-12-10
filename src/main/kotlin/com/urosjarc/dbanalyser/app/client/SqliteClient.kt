package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.column.ForeignKey
import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.*
import java.util.*


class SqliteClient(override val db: Db) : Client {

    var con: Connection? = null

    init {
        val info = Properties()
        info.set(key = "user", value = db.user)
        info.set(key = "password", value = db.password)
        try {
            this.con = DriverManager.getConnection(db.url, info);
        } catch (_: SQLException) {
        }
    }

    override fun inited() = con != null

    override fun tables(): List<Table> {
        val md: DatabaseMetaData = this.con!!.getMetaData()
        val rs: ResultSet = md.getTables(null, null, "%", null)
        val tables = mutableListOf<Table>()
        while (rs.next()) {
            val table = Table(name = rs.getString(3))
            val foreignKeys = mutableListOf<ForeignKey>()
            this.exec("SELECT * FROM pragma_foreign_key_list('${table.name}')") {
                foreignKeys.add(
                    ForeignKey(
                        tableName = it.getString("table"),
                        from = it.getString("from"),
                        to = it.getString("to")
                    )
                )
            }
            this.exec("SELECT * FROM pragma_table_info('${table.name}')") {
                val name = it.getString("name")
                table.columns.add(
                    Column(
                        name = name,
                        type = it.getString("type"),
                        notNull = it.getBoolean("notnull"),
                        defaultValue = it.getString("dflt_value"),
                        primaryKey = it.getBoolean("pk"),
                        foreignKey = foreignKeys.firstOrNull { fk -> fk.from == name}
                    )
                )
            }
            tables.add(table)
        }
        return tables
    }

    fun exec(sql: String, onResultSet: (rs: ResultSet) -> Unit) {
        try {
            this.con!!.createStatement().use { statement ->
                val rs = statement.executeQuery(sql)
                while (rs.next()) onResultSet(rs)
            }
        } catch (e: SQLException) {
            println(e)
        }
    }
}
