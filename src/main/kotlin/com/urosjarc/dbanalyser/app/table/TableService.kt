package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.ForeignKey


class TableService(
	val tableRepo: TableRepo
) {

	fun forwardConnections(table: Table): MutableList<ForeignKey> = table.foreignKeys.toMutableList()

	fun backwardConnections(table: Table): MutableList<ForeignKey> {
		val fkeys = mutableListOf<ForeignKey>()
		this.tableRepo.data.filter { it != table }.forEach { otherTable ->
			otherTable.foreignKeys.forEach { foreignKey ->
				if (foreignKey.to.table.name == table.name) {
					fkeys.add(foreignKey)
				}
			}
		}
		return fkeys
	}

	fun connections(table: Table): Set<ForeignKey> {
		return (this.forwardConnections(table = table) + this.backwardConnections(table = table)).toSet()
	}

	fun paths(startTable: Table, endTable: Table?, maxDepth: Int): TableConnection {
		val node = TableConnection(table = startTable, foreignKey = null)
		val queue = mutableListOf(node)

		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()

			if (endTable == null) current.alive = true

			// If recursive connection
			if (current.parent?.table?.name == current.table.name) continue

			// If dead end
			if (current.table.name == endTable?.name) {
				current.makeBranchAlive()
				continue
			}

			// If max depth
			if (current.depth >= maxDepth) continue

			val cons = this.connections(table = current.table)
				.filter { it.from.table.name != current.parent?.table?.name }

			cons.forEach {
//                current.connect(it)
//                queue.add(it)
			}
		}

		node.removeZombies()
		return node
	}
}
