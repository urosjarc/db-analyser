package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.shared.matchRatio
import com.urosjarc.dbanalyser.shared.setColumnWidth
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.apache.logging.log4j.kotlin.logger
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
	val log = this.logger()
	val tableRepo by this.inject<TableRepo>()

	var forwardOnNumberClicks = 0
	var forwardOnForeignColumnClick = false
	var forwardOnColumnClick = false
	var disableSearch = false

	@FXML
	fun initialize() {
		this.log.info(this.javaClass)
		this.columnTV.setOnMouseClicked { this.chose(it) }
		this.columnTF.setOnAction { this.search() }

		this.keyTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.meta) }
		this.typeTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.type) }
		this.schemaTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.table.schema?.name) }
		this.tableTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.table.name) }
		this.columnTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }
		this.toTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.connection) }

		this.columnTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
		setColumnWidth(this.keyTC, 10)
		setColumnWidth(this.typeTC, 10)
		setColumnWidth(this.schemaTC, 10)
		setColumnWidth(this.tableTC, 20)
		setColumnWidth(this.columnTC, 20)
		setColumnWidth(this.toTC, 30)
	}

	fun update(table: Table?) {
		this.columnTV.items.setAll(table?.columns ?: listOf())
	}

	private fun search() {
		if (!this.disableSearch) this.columnTV.items.sortByDescending { matchRatio(it.name, this.columnTF.text) }
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
