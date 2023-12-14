package com.urosjarc.dbanalyser.app.schema

import com.urosjarc.dbanalyser.app.table.Table
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Schema(
        val name: String,
        val tables: MutableList<Table> = mutableListOf()
) {
    override fun hashCode(): Int = this.name.hashCode()

    override fun toString(): String = this.name
}
