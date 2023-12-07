package com.urosjarc.dbanalyser.app.column

import kotlinx.serialization.Serializable

@Serializable
data class ForeignKey(
    val tableName: String,
    val from: String,
    val to: String
)
