package com.urosjarc.dbanalyser.app.commit

import com.urosjarc.dbanalyser.shared.Repository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.logger
import java.io.File

class CommitRepo(val fileName: String) : Repository<Commit>() {
    override val log = this.logger()

    init {
        this.load()
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
