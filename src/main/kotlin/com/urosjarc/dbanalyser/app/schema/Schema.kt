package com.urosjarc.dbanalyser.app.schema

import com.urosjarc.dbanalyser.app.table.Table
import kotlinx.serialization.Serializable

@Serializable
data class Schema(
    val name: String,
    val tables: MutableList<Table> = mutableListOf()
) {
    override fun toString(): String = this.name
    override fun hashCode(): Int = this.toString().hashCode()
    override fun equals(other: Any?): Boolean {
        if (other !is Schema) return false
        return this.toString() == other.toString()
    }
}
