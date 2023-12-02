package com.urosjarc.dbanalyser.app.db

data class Db(
    val name: String,
    val user: String,
    val password: String,
    val url: String,
    val type: Type
) {
    enum class Type { MYSQL, SQLITE}

    override fun toString(): String = "${this.name} ${this.url}"
}
