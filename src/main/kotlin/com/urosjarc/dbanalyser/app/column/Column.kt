package com.urosjarc.dbanalyser.app.column

data class Column(
    val name: String,
    val type: String,
    val notNull: Boolean,
    val primaryKey: Boolean,
    val foreignKey: ForeignKey? = null
)
