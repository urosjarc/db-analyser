package com.urosjarc.dbanalyser.app.db

import com.urosjarc.dbanalyser.shared.Repository

class DbRepo : Repository<Db>() {
    fun contains(db: Db): Boolean = this.data.any { it.name == db.name }
}
