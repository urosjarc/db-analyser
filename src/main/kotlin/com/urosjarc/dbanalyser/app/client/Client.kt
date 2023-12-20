package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.logs.LogService
import com.urosjarc.dbanalyser.app.schema.Schema
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

abstract class Client(db: Db) : KoinComponent {
	val log by this.inject<LogService>()

	var con: Connection? = null

	abstract fun schemas(): List<Schema>


	init {
		val info = Properties()
		info.set(key = "user", value = db.user)
		info.set(key = "password", value = db.password)
		try {
			this.con = DriverManager.getConnection(db.url, info);
			this.log.info("Db connected: $db")
		} catch (e: SQLException) {
			this.log.err(e.localizedMessage)
		}
	}

	fun inited() = con != null

	fun exec(sql: String, onResultSet: (rs: ResultSet) -> Unit) {
		this.log.debug("EXE SQL: $sql")
		try {
			this.con!!.createStatement().use { statement ->
				val rs = statement.executeQuery(sql)
				while (rs.next()) {
					onResultSet(rs)
				}
			}
		} catch (e: SQLException) {
			this.log.fatal(e.localizedMessage)
		}
	}

	fun <T : Any> execOne(sql: String, onResultSet: (rs: ResultSet) -> T?): T? {
		this.log.debug("EXE SQL: $sql")
		try {
			this.con!!.createStatement().use { statement ->
				val rs = statement.executeQuery(sql)
				if (rs.next()) return onResultSet(rs)
			}
		} catch (e: SQLException) {
			this.log.fatal(e.localizedMessage)
		}
		return null
	}
}
