package com.urosjarc.dbanalyser.app.column

import kotlinx.serialization.Serializable

@Serializable
data class Column(
    val name: String,
    val type: String,
    val notNull: Boolean,
    val primaryKey: Boolean,
    val foreignKey: ForeignKey? = null
) {
    val meta get(): String {
        val data = mutableListOf<String>()
        if (this.primaryKey) data.add("P")
        if (this.foreignKey != null) data.add("F")
        if (this.notNull) data.add("N")
        return data.joinToString()
    }

    val baseType get() = this.type.split("(").first()

    val connection get() = if (this.foreignKey != null) "${this.foreignKey.tableName}.${this.foreignKey.to}" else ""
}
