package com.urosjarc.dbanalyser.app.commit

import com.urosjarc.dbanalyser.app.schema.Schema
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Commit(
    val name: String,
    val created: Instant = Clock.System.now(),
    val schemas: Set<String>,
    val tables: Set<String>,
    val columns: Set<String>,
) {

    override fun toString(): String {
        val dt = this.created.toLocalDateTime(TimeZone.UTC)
        return "${dt.date} - ${dt.hour}.${dt.minute} | ${name.trim()}"
    }
    constructor(name: String, schemas: List<Schema>) : this(
        name = name,
        schemas = schemas.map { it.toString() }.toSet(),
        tables = schemas.flatMap { it.tables.map { it.toString() } }.toSet(),
        columns = schemas.flatMap { it.tables.flatMap { it.columns.map { it.signature() } } }.toSet()
    )

    fun diff(commit: Commit): CommitDiff {
        val cdiff = CommitDiff()

        val nowSchemas = this.schemas
        val beforeSchemas = commit.schemas
        cdiff.schemasCreated = nowSchemas - beforeSchemas
        cdiff.schemasDeleted = beforeSchemas - nowSchemas

        val nowTables = this.tables
        val beforeTables = commit.tables
        cdiff.tablesCreated = nowTables - beforeTables
        cdiff.tablesDeleted = beforeTables - nowTables

        val nowColumns = this.columns
        val beforeColumns = commit.columns
        cdiff.columnsCreated = nowColumns - beforeColumns
        cdiff.columnsDeleted = beforeColumns - nowColumns

        return cdiff
    }
}
