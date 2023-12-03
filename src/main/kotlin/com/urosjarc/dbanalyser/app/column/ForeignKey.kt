package com.urosjarc.dbanalyser.app.column

data class ForeignKey(
    val table: String,
    val from: String,
    val to: String
)
