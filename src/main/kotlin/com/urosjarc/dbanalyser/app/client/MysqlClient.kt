package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class MysqlClient(override val db: Db) : Client {

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
        return listOf()
    }
}
