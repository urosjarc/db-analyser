package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.schema.Schema
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Table(
    val schema: Schema?,
    val name: String,
    val created: Instant?,
    val modified: Instant?,
    val columns: MutableList<Column> = mutableListOf(),
    val escaping: Char
) {
    override fun toString(): String = """${this.schema?.name}.${escaping}${this.name}${escaping}"""
    override fun hashCode(): Int = this.toString().hashCode()
    override fun equals(other: Any?): Boolean {
        if (other !is Table) return false
        return other.toString() == this.toString()
    }

    fun uniqueShortName(taken: MutableSet<String>): String {
        var i = 1
        var sign = this.name.filter { it.isUpperCase() }
        val originalStartSign = sign
        while (taken.contains(sign)) sign = "$originalStartSign${i++}"
        return sign
    }

    val primaryKey get() = this.columns.filter { it.primaryKey }.firstOrNull()
    val foreignKeys get() = this.columns.mapNotNull { it.foreignKey }
}
