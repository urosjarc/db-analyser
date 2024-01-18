package com.urosjarc.dbanalyser.gui.widgets.tables

import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.tableConnection.TableConnection
import com.urosjarc.dbanalyser.app.tableConnection.TableConnectionRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import com.urosjarc.dbanalyser.gui.parts.TableConnectionTreeView
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class ConnectionSearchUi : KoinComponent {

	@FXML
	lateinit var startTableController: TableComboBox

	@FXML
	lateinit var endTableController: TableComboBox

	@FXML
	lateinit var tableTreeViewController: TableConnectionTreeView

	@FXML
	lateinit var searchB: Button
}

class ConnectionSearch : ConnectionSearchUi() {

	val tableRepo by this.inject<TableRepo>()
	val tableConnectionRepo by this.inject<TableConnectionRepo>()

	@FXML
	fun initialize() {
		this.tableConnectionRepo.onChose {
			this.startTableController.select(table = it.table)
			this.search()
		}
		this.searchB.setOnAction { this.search() }
		this.tableTreeViewController.self.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
			if (newValue != null) this.chose(newValue.value)
		}
	}

	fun search() {
		val startTable = startTableController.table ?: this.tableRepo.chosen ?: return
		val endTable = endTableController.table
		tableTreeViewController.update(startTable = startTable, endTable = endTable)
		tableRepo.chose(startTable)
	}

	fun chose(tableConnection: TableConnection) {
	}

}
