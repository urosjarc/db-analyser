package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableService
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableTreeViewUi : KoinComponent {
    @FXML
    lateinit var self: TreeTableView<TableConnection>

    @FXML
    lateinit var nameColumn: TreeTableColumn<TableConnection, String>

    @FXML
    lateinit var fromColumn: TreeTableColumn<TableConnection, String>

    @FXML
    lateinit var toColumn: TreeTableColumn<TableConnection, String>
}

class TableTreeView : TableTreeViewUi() {
    val tableService by this.inject<TableService>()

    @FXML
    fun initialize() {
        this.nameColumn.setCellValueFactory {
            ReadOnlyStringWrapper(it.value.value.table.name)
        }
        this.fromColumn.setCellValueFactory {
            ReadOnlyStringWrapper(it.value.value.connectionName(from = false))
        }
        this.toColumn.setCellValueFactory {
            ReadOnlyStringWrapper(it.value.value.connectionName(from = true))
        }
    }

    fun update(startTable: Table, endTable: Table?) {
        val tableConnection = this.tableService.paths(startTable, endTable, maxDepth = 6)
        this.self.root = TreeItem(tableConnection)
        this.self.root.isExpanded = true
        val queue = mutableListOf(this.self.root)
        while(queue.isNotEmpty()){
            val current = queue.removeFirst()
            current.value.childrens.forEach {
                val newNode = TreeItem(it)
                newNode.isExpanded = false
                current.children.add(newNode)
                queue.add(newNode)
            }
        }
    }

}
