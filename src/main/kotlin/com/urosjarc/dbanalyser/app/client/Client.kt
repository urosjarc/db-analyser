package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.table.Table

interface Client {
    val db: Db
    fun inited(): Boolean
    fun tables(): List<Table>

}
