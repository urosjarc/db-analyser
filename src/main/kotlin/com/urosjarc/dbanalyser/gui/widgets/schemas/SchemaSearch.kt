package com.urosjarc.dbanalyser.gui.widgets.schemas

import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.app.table.TableService
import com.urosjarc.dbanalyser.shared.matchRatio
import com.urosjarc.dbanalyser.shared.setColumnWidth
import com.urosjarc.dbanalyser.shared.startThread
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class SchemaSearchUi : KoinComponent {
	class Model(
		val schemaTC: String,
		val tablesTC: Int,
		val columnsTC: Int,
		val parentsTC: Int,
		val childrenTC: Int,
	) {
		val relativesTC: Int = this.parentsTC + this.childrenTC
	}

	@FXML
	lateinit var schemaTC: TableColumn<Model, String>

	@FXML
	lateinit var tablesTC: TableColumn<Model, Int>

	@FXML
	lateinit var columnsTC: TableColumn<Model, Int>

	@FXML
	lateinit var childrenTC: TableColumn<Model, Int>

	@FXML
	lateinit var parentsTC: TableColumn<Model, Int>

	@FXML
	lateinit var relativesTC: TableColumn<Model, Int>

	@FXML
	lateinit var modelTV: TableView<Model>

	@FXML
	lateinit var schemaTF: TextField
}

class SchemaSearch : SchemaSearchUi() {

	val schemaRepo by this.inject<SchemaRepo>()
	val tableService by this.inject<TableService>()

	@FXML
	fun initialize() {

		this.schemaRepo.onData { this.update() }
		this.schemaTF.setOnAction { this.search() }
		this.modelTV.selectionModel.selectedItemProperty().addListener { _, _, _ -> this.select() }

		this.schemaTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.schemaTC) }
		this.tablesTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.tablesTC) }
		this.columnsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.columnsTC) }
		this.parentsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.parentsTC) }
		this.childrenTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.childrenTC) }
		this.relativesTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.relativesTC) }

		this.modelTV.selectionModel.selectionMode = SelectionMode.MULTIPLE
		this.modelTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
		setColumnWidth(this.schemaTC, 50)
		setColumnWidth(this.tablesTC, 10)
		setColumnWidth(this.columnsTC, 10)
		setColumnWidth(this.parentsTC, 10)
		setColumnWidth(this.childrenTC, 10)
		setColumnWidth(this.relativesTC, 10)
	}

	private fun update() = startThread {
		this.modelTV.items.setAll(this.schemaRepo.data.map {
			Model(
				schemaTC = it.toString(),
				tablesTC = it.tables.size,
				columnsTC = it.tables.sumOf { tab -> tab.columns.size },
				parentsTC = it.tables.sumOf { tab -> tab.foreignKeys.size },
				childrenTC = it.tables.sumOf { tab -> this.tableService.children(table = tab).size }
			)
		})
	}

	private fun search() = startThread {
		this.modelTV.items.sortByDescending { matchRatio(it.schemaTC, this.schemaTF.text) }
	}

	private fun select() {
		val schemas = this.modelTV.selectionModel.selectedItems.mapNotNull { this.schemaRepo.find(it.schemaTC) }
		this.schemaRepo.select(schemas)
	}
}
