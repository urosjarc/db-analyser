package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.shared.matchRatio
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ColumnTableViewUi : KoinComponent {
	@FXML
	lateinit var columnTF: TextField

	@FXML
	lateinit var columnTV: TableView<Column>

	@FXML
	lateinit var toTC: TableColumn<Column, String>

	@FXML
	lateinit var typeTC: TableColumn<Column, String>

	@FXML
	lateinit var schemaTC: TableColumn<Column, String>

	@FXML
	lateinit var tableTC: TableColumn<Column, String>

	@FXML
	lateinit var columnTC: TableColumn<Column, String>

	@FXML
	lateinit var keyTC: TableColumn<Column, String>

}

class ColumnTableView : ColumnTableViewUi() {
	val tableRepo by this.inject<TableRepo>()

	var forwardOnNumberClicks = 0
	var forwardOnForeignColumnClick = false
	var forwardOnColumnClick = false

	@FXML
	fun initialize() {
		this.columnTV.setOnMouseClicked { this.chose(it) }
		this.columnTF.setOnAction { this.search() }

		this.keyTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.meta) }
		this.typeTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.type) }
		this.schemaTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.table.schema?.name) }
		this.tableTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.table.name) }
		this.columnTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }
		this.toTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.connection) }

		this.columnTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
		this.keyTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.typeTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.schemaTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.tableTC.maxWidth = (Integer.MAX_VALUE * 20.0)
		this.columnTC.maxWidth = (Integer.MAX_VALUE * 20.0)
		this.toTC.maxWidth = (Integer.MAX_VALUE * 30.0)
	}

	fun update(table: Table) {
		this.columnTV.items.setAll(table.columns)
	}
	private fun search() {
		this.columnTV.items.sortByDescending { matchRatio(it.name, this.columnTF.text) }
	}
	fun chose(mouseEvent: MouseEvent) {
		if (mouseEvent.clickCount == this.forwardOnNumberClicks) {
			val column = this.columnTV.selectionModel.selectedItem ?: return
			var table: Table? = null
			if (this.forwardOnColumnClick) table = column.table
			if (this.forwardOnForeignColumnClick) table = column.foreignKey?.to?.table
			if (table != null) this.tableRepo.chose(table)

		}
	}
}
