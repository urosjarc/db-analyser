package com.urosjarc.dbanalyser.gui.widgets.search

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import com.urosjarc.dbanalyser.shared.matchRatio
import com.urosjarc.dbanalyser.shared.startThread
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.layout.FlowPane
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ColumnSearchUi : KoinComponent {
	@FXML
	lateinit var typeFP: FlowPane

	@FXML
	lateinit var constrainedCB: CheckBox

	@FXML
	lateinit var defaultCB: CheckBox

	@FXML
	lateinit var notNullCB: CheckBox

	@FXML
	lateinit var uniqueCB: CheckBox

	@FXML
	lateinit var selfRefKeyCB: CheckBox

	@FXML
	lateinit var foreignKeyCB: CheckBox

	@FXML
	lateinit var primaryKeyCB: CheckBox

	@FXML
	lateinit var columnTableViewController: ColumnTableView

	@FXML
	lateinit var selectAllB: Button

	@FXML
	lateinit var deselectAllB: Button

}

class ColumnSearch : ColumnSearchUi() {

	val tableRepo by this.inject<TableRepo>()

	@FXML
	fun initialize() {
		this.columnTableViewController.also {
			it.forwardOnColumnClick = true
			it.forwardOnNumberClicks = 1
			it.disableSearch = true
			it.columnTF.setOnAction { this.search() }
		}

		this.tableRepo.onData { this.update() }
		this.selectAllB.setOnAction { this.selectAll(true) }
		this.deselectAllB.setOnAction { this.selectAll(false) }

		this.constrainedCB.setOnAction { this.search() }
		this.defaultCB.setOnAction { this.search() }
		this.notNullCB.setOnAction { this.search() }
		this.uniqueCB.setOnAction { this.search() }
		this.selfRefKeyCB.setOnAction { this.search() }
		this.foreignKeyCB.setOnAction { this.search() }
		this.primaryKeyCB.setOnAction { this.search() }
	}

	private fun selectAll(state: Boolean) {
		this.constrainedCB.isSelected = state
		this.defaultCB.isSelected = state
		this.notNullCB.isSelected = state
		this.uniqueCB.isSelected = state
		this.selfRefKeyCB.isSelected = state
		this.foreignKeyCB.isSelected = state
		this.primaryKeyCB.isSelected = state
		this.typeFP.children.forEach { if (it is CheckBox) it.isSelected = state }
		this.search()
	}

	private fun search() = startThread {
		val columns = mutableListOf<Column>()
		tableRepo.data.forEach { table ->
			for (column in table.columns) {
				if (primaryKeyCB.isSelected && column.primaryKey) {
					columns.add(column); continue
				}
				if (foreignKeyCB.isSelected && column.isForeignKey) {
					columns.add(column); continue
				}
				if (selfRefKeyCB.isSelected && column.foreignKey?.to?.table == table) {
					columns.add(column); continue
				}
				if (notNullCB.isSelected && column.notNull) {
					columns.add(column); continue
				}
				if (defaultCB.isSelected && column.hasDefaultValue) {
					columns.add(column); continue
				}

				for (child in typeFP.children) {
					if (child is CheckBox && child.isSelected && child.text.equals(column.baseType)) {
						columns.add(column)
						break
					}
				}
			}
		}

		columns.sortByDescending { matchRatio(it.name, this.columnTableViewController.columnTF.text) }

		columnTableViewController.columnTV.items.setAll(columns)

	}

	fun update() = startThread {
		val baseTypes = mutableSetOf<String>()
		this.tableRepo.data.forEach { table ->
			table.columns.forEach { column: Column ->
				if (column.baseType.isNotBlank()) baseTypes.add(column.baseType)
			}
		}

		this.typeFP.children.setAll(FXCollections.observableArrayList(baseTypes.map { type ->
			CheckBox(type).also { cb ->
				cb.isSelected = true
				cb.setOnAction { this.search() }
			}
		}))
		this.search()
	}
}
