package com.urosjarc.dbanalyser.app.column

import kotlinx.serialization.Serializable

@Serializable
data class Column(
    val name: String,
    val type: String,
    val notNull: Boolean,
    val primaryKey: Boolean,
    val foreignKey: ForeignKey? = null
)
