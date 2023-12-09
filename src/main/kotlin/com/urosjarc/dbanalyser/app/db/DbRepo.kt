package com.urosjarc.dbanalyser.app.db

import com.urosjarc.dbanalyser.shared.Repository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class DbRepo(val fileName: String) : Repository<Db>() {
    init {
        this.load()
    }

    override fun load() {
        val file = File(this.fileName)
        if (!file.exists()) return
        this.setAll(Json.decodeFromString(file.readText()))
    }

    override fun save() {
        val file = File(this.fileName)
        if (!file.exists()) file.createNewFile()
        file.writeText(Json.encodeToString(this.data))
    }
    fun contains(db: Db): Boolean = this.data.any { it.name == db.name }

}
