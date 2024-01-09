package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import com.urosjarc.dbanalyser.shared.setColumnWidth
import com.urosjarc.dbanalyser.shared.startThread
import javafx.application.Platform
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.value.ChangeListener
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class TableConnectionTreeItem(val tableConnection: TableConnection) : TreeItem<TableConnection>(tableConnection), KoinComponent {

	val clientService by this.inject<ClientService>()
	val tableService by this.inject<TableService>()
	var wasOpen = false

	private val maxRelationsListener: ChangeListener<in Number> = ChangeListener<Number> { _, _, _ ->
		val event = TreeModificationEvent(valueChangedEvent<TableConnectionTreeView>(), this)
		Event.fireEvent(this, event)
	}

	init {
		this.tableConnection.maxRelations.addListener(this.maxRelationsListener)
		this.valueProperty().addListener { _, oldValue, newValue ->
			oldValue?.maxRelations?.removeListener(this.maxRelationsListener)
			newValue?.maxRelations?.addListener(this.maxRelationsListener)
		}
		this.expandedProperty().addListener { _, _, newValue ->
			if (newValue && !this.wasOpen) {
				this.wasOpen = true
				this.tableConnection.children.forEach {
					it.maxRelations.value = this.clientService.countMaxRelations(it)
				}
				this.children.forEach { tc ->
					val tableConnection = this.tableService.paths(tc.value.table, null, maxDepth = 1)
					tableConnection.children.forEach {
						tc.children.add(TableConnectionTreeItem(it))
						tc.value.connect(it)
					}
				}
			}
		}
	}
}

open class TableConnectionTreeViewUi : KoinComponent {
	@FXML
	lateinit var self: TreeTableView<TableConnection>

	@FXML
	lateinit var nameTC: TreeTableColumn<TableConnection, String>

	@FXML
	lateinit var fromTC: TreeTableColumn<TableConnection, String>

	@FXML
	lateinit var toTC: TreeTableColumn<TableConnection, String>

	@FXML
	lateinit var maxRelationsTC: TreeTableColumn<TableConnection, String>
}

class TableConnectionTreeView : TableConnectionTreeViewUi() {
	val tableService by this.inject<TableService>()
	val tableRepo by this.inject<TableRepo>()

	@FXML
	fun initialize() {
		this.self.selectionModel.selectedItemProperty().addListener { _, _, newValue -> this.chose(newValue) }
		this.nameTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.table.toString()) }
		this.fromTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.connectionName(from = true)) }
		this.toTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.connectionName(from = false)) }
		this.maxRelationsTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.value.maxRelations.value.toString()) }

		this.self.columnResizePolicy = TreeTableView.CONSTRAINED_RESIZE_POLICY;
		setColumnWidth(this.nameTC, 33)
		setColumnWidth(this.fromTC, 33)
		setColumnWidth(this.toTC, 33)
		setColumnWidth(this.maxRelationsTC, 10)
	}

	fun update(startTable: Table, endTable: Table?) = startThread {
		val tableConnection = this.tableService.paths(startTable, endTable, maxDepth = 2)

		Platform.runLater { //SAFE!!!
			this.self.root = TableConnectionTreeItem(tableConnection)
			this.self.root.isExpanded = true
			val queue = mutableListOf(this.self.root)
			while (queue.isNotEmpty()) {
				val current = queue.removeFirst()
				current.value.children.forEach {
					val newNode = TableConnectionTreeItem(it)
					newNode.isExpanded = false
					current.children.add(newNode)
					queue.add(newNode)
				}
			}
		}
	}

	fun chose(treeItem: TreeItem<TableConnection>?) {
		if (treeItem == null) return
		if (treeItem.value != null) {
			this.tableRepo.chose(treeItem.value.table)
		}
	}

}
