package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.Column

data class Table(
    val name: String,
    val columns: List<Column>
)
