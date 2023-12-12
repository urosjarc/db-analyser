package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.Column
import kotlinx.serialization.Serializable

@Serializable
data class Table(
    val name: String,
    val columns: MutableList<Column> = mutableListOf(),
    var rows: Int = 0
) {
    override fun toString(): String {
        return this.name
    }

    val foreignKeys get() = this.columns.mapNotNull { it.foreignKey }
}
