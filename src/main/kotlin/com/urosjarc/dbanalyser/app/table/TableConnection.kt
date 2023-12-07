package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.ForeignKey

data class TableConnection(
    val table: Table,
    val foreignKey: ForeignKey?,
    var parent: TableConnection? = null,
    val childrens: MutableList<TableConnection> = mutableListOf(),
    var alive: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is TableConnection -> {
                val equalTableName = other.table.name == this.table.name
                val equalParentName = other.parent?.table?.name == this.parent?.table?.name
                val equalForeignKey = other.foreignKey == this.foreignKey
                equalTableName && equalParentName && equalForeignKey
            }
            else -> super.equals(other)
        }
    }

    fun makeBranchAlive(){
        this.alive = true
        var node: TableConnection? = this
        while (node?.parent != null) {
            node = node.parent
            node?.alive = true
        }
    }

    fun removeZombies(){
        this.childrens.removeIf { !it.alive }
        this.childrens.forEach { it.removeZombies() }
    }

    fun connect(con: TableConnection) {
        con.parent = this
        this.childrens.add(con)
    }

    override fun toString(): String {
        return "${this.table.name} -> ${this.foreignKey}"
    }

    val depth
        get(): Int {
            var count = 0
            var node: TableConnection? = this
            while (node?.parent != null) {
                count++
                node = node.parent
            }
            return count
        }

    fun connectionName(from: Boolean): String {
        val string = if (this.forwardConnection)
            if (from) "${this.parent?.table?.name}.${this.foreignKey?.from}"
            else "${this.table.name}.${this.foreignKey?.to}"
        else
            if (from) "${this.table.name}.${this.foreignKey?.to}"
            else "${this.parent?.table?.name}.${this.foreignKey?.from}"

        if (string.contains("null")) return ""
        return string
    }

    val backwardConnection get() = this.foreignKey?.tableName == table.name
    val forwardConnection get() = !this.backwardConnection
}
