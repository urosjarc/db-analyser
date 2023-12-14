package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.schema.Schema


class SqliteClient(db: Db) : Client(db) {
    override fun schemas(): List<Schema> {
        TODO("Not yet implemented")
    }


}
