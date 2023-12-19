package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import com.urosjarc.dbanalyser.shared.setColumnWidth
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
	lateinit var nameTC: TreeTableColumn<TableConnection, String>

	@FXML
	lateinit var fromTC: TreeTableColumn<TableConnection, String>

	@FXML
	lateinit var toTC: TreeTableColumn<TableConnection, String>
}

class TableTreeView : TableTreeViewUi() {
	val tableService by this.inject<TableService>()
	val tableRepo by this.inject<TableRepo>()

	@FXML
	fun initialize() {
		this.self.selectionModel.selectedItemProperty().addListener { _, _, newValue -> this.chose(newValue?.value) }
		this.nameTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.table.toString()) }
		this.fromTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.connectionName(from = true)) }
		this.toTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.connectionName(from = false)) }

		this.self.columnResizePolicy = TreeTableView.CONSTRAINED_RESIZE_POLICY;
		setColumnWidth(this.nameTC, 40)
		setColumnWidth(this.fromTC, 30)
		setColumnWidth(this.toTC, 30)
	}

	fun update(startTable: Table, endTable: Table?) {
		val tableConnection = this.tableService.paths(startTable, endTable, maxDepth = 3)
		this.self.root = TreeItem(tableConnection)
		this.self.root.isExpanded = true
		val queue = mutableListOf(this.self.root)
		while (queue.isNotEmpty()) {
			val current = queue.removeFirst()
			current.value.childrens.forEach {
				val newNode = TreeItem(it)
				newNode.isExpanded = false
				current.children.add(newNode)
				queue.add(newNode)
			}
		}
	}

	fun chose(tableConnection: TableConnection?) {
		if (tableConnection != null)
			this.tableRepo.chose(tableConnection.table)
	}

}
