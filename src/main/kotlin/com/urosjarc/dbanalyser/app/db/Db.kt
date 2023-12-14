package com.urosjarc.dbanalyser.app.db

import kotlinx.serialization.Serializable

@Serializable
data class Db(
    val name: String,
    val user: String,
    val password: String,
    val url: String,
    val type: Type
) {
    enum class Type { SQLITE, MS_SQL}

    override fun equals(other: Any?): Boolean = when (other) {
        is Db -> this.name == other.name
        else -> super.equals(other)
    }
    override fun toString(): String = "${this.name} ${this.url}"
}
