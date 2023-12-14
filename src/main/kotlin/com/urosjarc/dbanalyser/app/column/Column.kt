package com.urosjarc.dbanalyser.app.column

import com.urosjarc.dbanalyser.app.table.Table
import kotlinx.serialization.Serializable

@Serializable
data class Column(
        val table: Table,
        val name: String,
        val type: String,
        val notNull: Boolean,
        val defaultValue: String?,
        val primaryKey: Boolean,
        val foreignKey: ForeignKey? = null
) {
    val meta
        get(): String {
            val data = mutableListOf<String>()
            if (this.primaryKey) data.add("P")
            if (this.foreignKey != null) data.add("F")
            if (this.notNull) data.add("N")
            return data.joinToString()
        }

    val isForeignKey get() = this.foreignKey != null

    val hasDefaultValue get() = this.defaultValue != null

    val baseType get() = this.type.split("(").first()

    val connection get() = if (this.isForeignKey) "${this.foreignKey?.to}" else ""
}
