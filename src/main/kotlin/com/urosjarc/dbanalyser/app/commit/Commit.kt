package com.urosjarc.dbanalyser.app.commit

import com.urosjarc.dbanalyser.app.schema.Schema
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Commit(
    val created: Instant = Clock.System.now(),
    val name: String,
    val schemas: Set<String>,
    val tables: Set<String>,
    val columns: Set<String>,
) {
    fun diff(schemas: List<Schema>): CommitDiff {
        val cdiff = CommitDiff()

        val beforeSchemas = this.schemas
        val nowSchemas = schemas.map { it.toString() }.toSet()
        cdiff.schemasCreated = nowSchemas - beforeSchemas
        cdiff.schemasDeleted = beforeSchemas - nowSchemas

        val beforeTables = this.tables
        val nowTables = schemas.flatMap { it.tables.map { it.toString() } }.toSet()
        cdiff.tablesCreated = nowTables - beforeTables
        cdiff.tablesDeleted = beforeTables - nowTables

        val beforeColumns = this.columns
        val nowColumns = schemas.flatMap { it.tables.flatMap { it.columns.map { it.toString() } } }.toSet()
        cdiff.columnsCreated = nowColumns - beforeColumns
        cdiff.columnsDeleted = beforeColumns - nowColumns

        return cdiff
    }
}
