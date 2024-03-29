package com.urosjarc.dbanalyser.app.db

import com.urosjarc.dbanalyser.app.commit.Commit
import com.urosjarc.dbanalyser.app.query.Query
import kotlinx.serialization.Serializable

@Serializable
data class Db(
    val name: String,
    var user: String,
    var password: String,
    var url: String,
    var type: Type,
    var queries: MutableList<Query> = mutableListOf(),
    var commits: MutableList<Commit> = mutableListOf()
) {
    enum class Type { SQLITE, MS_SQL, POSTGRESQL, MYSQL, ORACLE, MARIA, DB2, H2 }

    override fun hashCode(): Int {
        return "${this.type}${this.name}".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Db) return false
        return this.type == other.type && this.name == other.name
    }

    override fun toString(): String = "${this.type} | ${this.name} -> ${this.url}"

    fun merge(db: Db): Boolean {
        if (db == this) {
            this.user = db.user
            this.password = db.password
            this.url = db.url
            this.password = db.password
            this.queries.addAll(db.queries)
            return true
        }
        return false
    }
}
