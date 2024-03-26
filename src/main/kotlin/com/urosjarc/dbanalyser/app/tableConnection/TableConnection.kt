package com.urosjarc.dbanalyser.app.tableConnection

import com.urosjarc.dbanalyser.app.column.ForeignKey
import com.urosjarc.dbanalyser.app.table.Table
import javafx.beans.property.SimpleIntegerProperty

class TableConnection(
    val isParent: Boolean,
    val table: Table,
    val foreignKey: ForeignKey?,
    var maxRelations: SimpleIntegerProperty = SimpleIntegerProperty(-1),
    var parent: TableConnection? = null,
    val children: MutableSet<TableConnection> = mutableSetOf(),
    var alive: Boolean = false
) {

    override fun toString(): String {
        return "${this.start()?.table} -> ${this.table}"
    }

    override fun hashCode(): Int = this.toString().hashCode()
    override fun equals(other: Any?): Boolean {
        if (other !is TableConnection) return false
        return other.toString() == this.toString()
    }

    fun makeBranchAlive() {
        this.alive = true
        var node: TableConnection? = this
        while (node?.parent != null) {
            node = node.parent
            node?.alive = true
        }
    }

    fun removeZombies() {
        this.children.removeIf { !it.alive }
        this.children.forEach { it.removeZombies() }
    }

    fun connect(con: TableConnection) {
        con.parent = this
        this.children.add(con)
    }

    fun depth(): Int {
        var count = 0
        var node: TableConnection? = this
        while (node?.parent != null) {
            count++
            node = node.parent
        }
        return count
    }

    fun start(): TableConnection? {
        var count = 0
        var node: TableConnection? = this
        while (node?.parent != null) {
            count++
            node = node.parent
        }
        return node
    }

    fun connectionName(from: Boolean): String = this.foreignKey?.let {
        if (from) it.from.toString()
        else it.to.toString()
    } ?: ""
}
