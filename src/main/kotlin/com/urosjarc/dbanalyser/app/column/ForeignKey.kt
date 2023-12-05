package com.urosjarc.dbanalyser.app.column

import kotlinx.serialization.Serializable

@Serializable
data class ForeignKey(
    val table: String,
    val from: String,
    val to: String
)
