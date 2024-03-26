package com.urosjarc.dbanalyser.app.column

import kotlinx.serialization.Serializable

@Serializable
data class ForeignKey(
    val from: Column,
    val to: Column
) {
    override fun toString(): String = this.from.toString()
    override fun hashCode(): Int = this.toString().hashCode()
    override fun equals(other: Any?): Boolean = other.toString() == this.toString()
}
