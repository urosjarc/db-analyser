package com.urosjarc.dbanalyser.app.table


class TableService(
    val tableRepo: TableRepo
) {

    fun forwardConnections(table: Table): MutableList<TableConnection> {
        val tableCons = mutableListOf<TableConnection>()
        table.foreignKeys.forEach { foreignKey ->
            val foreignTable = this.tableRepo.find(foreignKey.from.table.name)
            if (foreignTable != null) tableCons.add(TableConnection(table = foreignTable, foreignKey = foreignKey))
        }
        return tableCons
    }

    fun backwardConnections(table: Table): MutableList<TableConnection> {
        val tableCons = mutableListOf<TableConnection>()
        this.tableRepo.data.forEach { otherTable ->
            otherTable.foreignKeys.forEach { foreignKey ->
                if (foreignKey.from.table.name == table.name) {
                    tableCons.add(TableConnection(table = otherTable, foreignKey = foreignKey))
                }
            }
        }
        return tableCons
    }

    fun connections(table: Table): Set<TableConnection> {
        return (this.forwardConnections(table = table) + this.backwardConnections(table = table)).toSet()
    }

    fun paths(startTable: Table, endTable: Table?, maxDepth: Int): TableConnection {
        val node = TableConnection(table = startTable, foreignKey = null)
        val queue = mutableListOf(node)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if(endTable == null) current.alive = true

            // If recursive connection
            if(current.parent?.table?.name == current.table.name) continue

            // If dead end
            if(current.table.name == endTable?.name){
                current.makeBranchAlive()
                continue
            }

            // If max depth
            if(current.depth >= maxDepth) continue

            val cons = this.connections(table = current.table)
                .filter { it.table.name != current.parent?.table?.name }

            cons.forEach {
                current.connect(it)
                queue.add(it)
            }
        }

        node.removeZombies()
        return node
    }
}
