package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.schema.Schema
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Table(
		val schema: Schema?,
		val name: String,
		val created: Instant,
		val modified: Instant,
		val columns: MutableList<Column> = mutableListOf()
) {
	override fun toString(): String = "${this.schema}.${this.name}"

	override fun hashCode(): Int = "${this.schema?.name}.${this.name}".hashCode()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Table
		return other.hashCode() == this.hashCode()
	}

	val foreignKeys get() = this.columns.mapNotNull { it.foreignKey }
}
