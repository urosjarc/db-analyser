package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.ForeignKey

class TableConnection(
	val isParent: Boolean,
	val table: Table,
	val foreignKey: ForeignKey?,
	var parent: TableConnection? = null,
	val childrens: MutableList<TableConnection> = mutableListOf(),
	var alive: Boolean = false
) {
	fun makeBranchAlive() {
		this.alive = true
		var node: TableConnection? = this
		while (node?.parent != null) {
			node = node.parent
			node?.alive = true
		}
	}

	fun removeZombies() {
		this.childrens.removeIf { !it.alive }
		this.childrens.forEach { it.removeZombies() }
	}

	fun connect(con: TableConnection) {
		con.parent = this
		this.childrens.add(con)
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

	fun connectionName(from: Boolean): String = this.foreignKey.let {
		if (from) it?.from.toString()
		else it?.to.toString()
	}
}
