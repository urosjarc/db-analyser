package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.schema.Schema
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Table(
        val schema: Schema?,
        val name: String,
        val created: Instant,
        val modified: Instant,
        val columns: MutableList<Column> = mutableListOf()
) {
    override fun toString(): String {
        return this.name
    }

    val foreignKeys get() = this.columns.mapNotNull { it.foreignKey }
}
