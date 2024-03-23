package com.urosjarc.dbanalyser.app.query

import kotlinx.serialization.Serializable

@Serializable
data class Query(
	val name: String,
	val type: Type,
	val sql: String,
	val dbMessiahSql: String
) {
	enum class Type { SELECT, UPDATE, INSERT, DELETE, CREATE }
	enum class Format {DB_MESSIAH}
}
