package com.urosjarc.dbanalyser.gui.widgets.search

import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import com.urosjarc.dbanalyser.gui.parts.TableConnectionTreeView
import com.urosjarc.dbanalyser.shared.startThread
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextArea
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

	@FXML
	lateinit var connectionTA: TextArea
}

class ConnectionSearch : ConnectionSearchUi() {

	val tableRepo by this.inject<TableRepo>()
	val clientService by this.inject<ClientService>()

	@FXML
	fun initialize() {
		this.searchB.setOnAction { this.search() }
		this.tableTreeViewController.self.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
			if (newValue != null) this.chose(newValue.value)
		}
	}

	fun search() {
		val startTable = startTableController.table ?: return
		val endTable = endTableController.table
		tableTreeViewController.update(startTable = startTable, endTable = endTable)
		tableRepo.chose(startTable)
	}

	fun chose(tableConnection: TableConnection) {
		this.connectionTA.text = this.clientService.joinSql(endTableConnection = tableConnection, countRelations = false)
	}

}
