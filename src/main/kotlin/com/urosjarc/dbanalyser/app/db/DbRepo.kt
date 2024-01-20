package com.urosjarc.dbanalyser.app.db

import com.urosjarc.dbanalyser.shared.Repository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.logger
import java.io.File

class DbRepo(val fileName: String) : Repository<Db>() {
	override val log = this.logger()

	init {
		this.load()
	}

	fun delete(dbName: String, dbType: Db.Type?) {
		this.data.firstOrNull { it.name == dbName && it.type == dbType }?.let {
			this.delete(it)
		}
	}

	override fun save(t: Db): Db {
		val oldDb = this.find(t)
		if (oldDb == null) {
			super.save(t)
			return t
		} else {
			oldDb.merge(db = t)
			this.onDataNotify()
			return oldDb
		}
	}

	override fun load() {
		val file = File(this.fileName)
		if (!file.exists()) return
		this.set(Json.decodeFromString(file.readText()))
	}

	override fun save() {
		val file = File(this.fileName)
		if (!file.exists()) file.createNewFile()
		file.writeText(Json.encodeToString(this.data))
	}

}
