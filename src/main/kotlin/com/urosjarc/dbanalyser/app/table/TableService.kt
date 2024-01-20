package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.column.ForeignKey
import com.urosjarc.dbanalyser.app.tableConnection.TableConnection


class TableService(
	val tableRepo: TableRepo,
) {

	fun parents(table: Table?): MutableList<ForeignKey> = table?.foreignKeys?.toMutableList() ?: mutableListOf()

	fun children(table: Table?): MutableList<ForeignKey> {
		val fkeys = mutableListOf<ForeignKey>()
		this.tableRepo.data.filter { it != table }.forEach { otherTable ->
			otherTable.foreignKeys.forEach { foreignKey ->
				if (foreignKey.to.table.name == table?.name) {
					fkeys.add(foreignKey)
				}
			}
		}
		return fkeys
	}

	fun paths(startTable: Table, endTable: Table?, maxDepth: Int): TableConnection {
		val node = TableConnection(isParent = false, table = startTable, foreignKey = null)
		val queue = mutableListOf(node)

		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()

			// If end is not known show everything
			if (endTable == null) current.alive = true

			// If recursive connection
			if (current.parent?.table?.name == current.table.name) continue

			// If dead end
			if (current.table.name == endTable?.name) {
				current.makeBranchAlive()
				continue
			}

			// If max depth reached skip
			if (current.depth() >= maxDepth) continue

			// Get parent connections for current table
			val parents = this.parents(table = current.table)
			parents
				//Skip double self reference
				.filter { it.to.table != current.parent?.table }
				.forEach {
					val tc = TableConnection(isParent = false, table = it.to.table, foreignKey = it, parent = current)
					current.connect(tc)
					queue.add(tc)
				}

			//Get children connections for current table
			val children = this.children(table = current.table)
			children
				//Skip double self reference
				.filter { it.from.table != current.parent?.table }
				.forEach {
					val tc = TableConnection(isParent = true, table = it.from.table, foreignKey = it, parent = current)
					current.connect(tc)
					queue.add(tc)
				}
		}

		node.removeZombies()
		return node
	}
}
