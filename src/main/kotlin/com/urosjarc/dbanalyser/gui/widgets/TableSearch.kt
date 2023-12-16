package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import com.urosjarc.dbanalyser.shared.matchRatio
import com.urosjarc.dbanalyser.shared.startThread
import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableSearchUi : KoinComponent {
	class Model(
		val schemaTC: String,
		val tableTC: String,
		val columnsTC: Int,
		val parentsTC: Int,
		val childrenTC: Int,
	) {
		val relativesTC: Int = this.parentsTC + this.childrenTC
	}

	@FXML
	lateinit var tableTF: TextField

	@FXML
	lateinit var modelTV: TableView<Model>

	@FXML
	lateinit var relativesTC: TableColumn<Model, Int>

	@FXML
	lateinit var parentsTC: TableColumn<Model, Int>

	@FXML
	lateinit var childrenTC: TableColumn<Model, Int>

	@FXML
	lateinit var columnsTC: TableColumn<Model, Int>

	@FXML
	lateinit var schemaTC: TableColumn<Model, String>

	@FXML
	lateinit var tableTC: TableColumn<Model, String>
}

class TableSearch : TableSearchUi() {
	val tableRepo by this.inject<TableRepo>()
	val tableService by this.inject<TableService>()

	@FXML
	fun initialize() {
		this.tableRepo.onChange { this.update() }
		this.modelTV.setOnMouseClicked { this.onItemClicked(it) }
		this.tableTF.setOnAction { this.search() }

		this.schemaTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.schemaTC) }
		this.tableTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.tableTC) }
		this.columnsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.columnsTC) }
		this.parentsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.parentsTC) }
		this.childrenTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.childrenTC) }
		this.relativesTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.relativesTC) }

		this.modelTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
		this.relativesTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.parentsTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.childrenTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.columnsTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.schemaTC.maxWidth = (Integer.MAX_VALUE * 10.0)
		this.tableTC.maxWidth = (Integer.MAX_VALUE * 50.0)
	}

	private fun onItemClicked(mouseEvent: MouseEvent) {
		if (mouseEvent.clickCount == 1) {
			val table: Table = this.tableRepo.find(this.modelTV.selectionModel.selectedItem.tableTC)!!
			this.tableRepo.select(table)
		}
	}

	private fun update() = startThread {
		this.modelTV.items.setAll(this.tableRepo.data.map {
			Model(
				schemaTC = it.schema.toString(),
				tableTC = it.name,
				columnsTC = it.columns.size,
				parentsTC = it.foreignKeys.size,
				childrenTC = this.tableService.backwardConnections(table = it).size
			)
		})
	}

	private fun search() = startThread {
		modelTV.items.sortByDescending { matchRatio(it.tableTC, tableTF.text) }
	}
}
