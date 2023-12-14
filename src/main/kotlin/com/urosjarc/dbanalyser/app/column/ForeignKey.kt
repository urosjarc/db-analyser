package com.urosjarc.dbanalyser.app.column

import kotlinx.serialization.Serializable

@Serializable
data class ForeignKey(
		val from: Column,
		val to: Column
) {
	override fun toString(): String = this.from.toString()
}
