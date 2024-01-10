package com.urosjarc.dbanalyser.app.db

import com.urosjarc.dbanalyser.app.query.Query
import kotlinx.serialization.Serializable

@Serializable
data class Db(
	val name: String,
	val user: String,
	val password: String,
	val url: String,
	val type: Type,
	val queries: MutableList<Query> = mutableListOf()
) {
	enum class Type { SQLITE, MS_SQL }

	override fun hashCode(): Int {
		return this.name.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		return this.hashCode() == other.hashCode()
	}

	override fun toString(): String = "${this.type} | ${this.name} -> ${this.url}"
}
