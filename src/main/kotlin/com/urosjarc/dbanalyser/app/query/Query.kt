package com.urosjarc.dbanalyser.app.query

import kotlinx.serialization.Serializable

@Serializable
data class Query(
	val name: String,
	val type: Type,
	val sql: String
) {
	enum class Type { SELECT, UPDATE, INSERT, DELETE, CREATE }
}
