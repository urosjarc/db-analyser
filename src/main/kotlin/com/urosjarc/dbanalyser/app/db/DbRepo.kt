package com.urosjarc.dbanalyser.app.db

import com.urosjarc.dbanalyser.shared.Repository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.inject
import java.io.File

class DbRepo : Repository<Db>() {
    override val file = File("db.json")
    val json by this.inject<Json>()

    init {
        this.load()
    }

    fun contains(db: Db): Boolean = this.data.any { it.name == db.name }


    override fun load() {
        if (!file.exists()) return
        this.setAll(this.json.decodeFromString(file.readText()))
    }

    override fun save() {
        if (!file.exists()) file.createNewFile()
        file.writeText(this.json.encodeToString(this.data))
    }
}
