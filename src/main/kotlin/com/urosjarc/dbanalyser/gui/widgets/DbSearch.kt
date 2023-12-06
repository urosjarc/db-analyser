package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class DbSearchUi : KoinComponent {
    @FXML
    lateinit var startTableController: TableComboBox

    @FXML
    lateinit var endTableController: TableComboBox

    @FXML
    lateinit var searchB: Button

    @FXML
    lateinit var tableTTV: TreeTableView<Table>
}

class DbSearch : DbSearchUi() {

    val tableRepo by this.inject<TableRepo>()

    @FXML
    fun initialize() {
        this.searchB.setOnAction { this.search() }
        val column = TreeTableColumn<Table, String>("Name")
        column.setCellValueFactory { p -> ReadOnlyStringWrapper(p.getValue().getValue().name) }
        this.tableTTV.columns.add(column)
    }

    fun search() {
        val startTable = this.startTableController.table!!
        val endTable = this.endTableController.table
        val queue = mutableListOf(mutableListOf(startTable))
        val results = mutableListOf<MutableList<Table>>()

        while (queue.isNotEmpty()) {
            val tableList = queue.removeFirst()
            val foreignKeys = tableList.last().columns.map { it.foreignKey }.filterNotNull()
            foreignKeys.forEach { fkey ->
                val table = this.tableRepo.find(fkey.table)
                val newList = tableList.toMutableList()
                if (table != null) {
                    if (!newList.contains(table)) {
                        newList.add(table)
                        queue.add(newList)
                    }
                    if (endTable == null || table == endTable) results.add(newList)
                }
            }
        }

        this.tableTTV.root = TreeItem(startTable)
        this.tableTTV.root.isExpanded = true
        results.forEach { lst ->
            val root = this.tableTTV.root
            var node = root
            lst.removeFirst()
            lst.forEach { table ->
                var item = node.children.find { it.value.name == table.name }
                if (item == null) {
                    item = TreeItem(table)
                    item.isExpanded = endTable != null
                    node.children.add(item)
                }
                node = item
            }
        }

    }

}
