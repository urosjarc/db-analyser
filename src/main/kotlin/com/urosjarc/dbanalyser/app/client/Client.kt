package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.schema.Schema
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

abstract class Client(val db: Db) {

	var con: Connection? = null

	abstract fun schemas(): List<Schema>


	init {
		val info = Properties()
		info.set(key = "user", value = db.user)
		info.set(key = "password", value = db.password)
		try {
			this.con = DriverManager.getConnection(db.url, info);
		} catch (_: SQLException) {
		}
	}

	fun inited() = con != null

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
