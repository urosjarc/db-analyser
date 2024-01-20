package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.column.ForeignKey
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
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

open class ForwardConnectionTableViewUi : KoinComponent {
	@FXML
	lateinit var tableTF: TextField

	@FXML
	lateinit var self: TableView<ForeignKey>

	@FXML
	lateinit var fromTC: TableColumn<ForeignKey, String>

	@FXML
	lateinit var columnTC: TableColumn<ForeignKey, String>

	@FXML
	lateinit var tableTC: TableColumn<ForeignKey, String>

	@FXML
	lateinit var schemaTC: TableColumn<ForeignKey, String>
}

class ParentConnectionTableView : ForwardConnectionTableViewUi() {
	val log = this.logger()
	val tableRepo by this.inject<TableRepo>()
	val tableService by this.inject<TableService>()

	@FXML
	fun initialize() {
		this.log.info(this.javaClass)
		this.tableRepo.onChose { this.update(it) }
		this.self.setOnMouseClicked { this.chose(it) }
		this.tableTF.setOnAction { this.search() }

		this.fromTC.setCellValueFactory { val fkey = it.value; ReadOnlyStringWrapper(fkey.from.name) }
		this.schemaTC.setCellValueFactory { val fkey = it.value; ReadOnlyStringWrapper(fkey.to.table.schema?.name) }
		this.tableTC.setCellValueFactory { val fkey = it.value; ReadOnlyStringWrapper(fkey.to.table.name) }
		this.columnTC.setCellValueFactory { val fkey = it.value; ReadOnlyStringWrapper(fkey.to.name) }

		this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
		setColumnWidth(this.fromTC, 40)
		setColumnWidth(this.schemaTC, 20)
		setColumnWidth(this.tableTC, 20)
		setColumnWidth(this.columnTC, 20)
	}

	private fun search() {
		this.self.items.sortByDescending { matchRatio(it.to.table.name, this.tableTF.text) }
	}

	private fun chose(mouseEvent: MouseEvent) {
		if (mouseEvent.clickCount == 2) {
			val fkey = this.self.selectionModel.selectedItem
			this.tableRepo.chose(fkey.to.table)
		}
	}

	fun update(table: Table?) {
		this.self.items.setAll(this.tableService.parents(table = table))
	}
}
