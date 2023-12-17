package com.urosjarc.dbanalyser.gui.widgets.tables

import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import com.urosjarc.dbanalyser.gui.parts.TableTreeView
import com.urosjarc.dbanalyser.shared.startThread
import javafx.application.Platform
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
	lateinit var tableTreeViewController: TableTreeView

	@FXML
	lateinit var searchB: Button


}

class ConnectionSearch : ConnectionSearchUi() {

	val tableRepo by this.inject<TableRepo>()

	@FXML
	fun initialize() {
		this.searchB.setOnAction { this.search() }
	}

	fun search() = startThread {
		Platform.runLater {
			val startTable = startTableController.table ?: return@runLater
			val endTable = endTableController.table
			tableTreeViewController.update(startTable = startTable, endTable = endTable)
			tableRepo.chose(startTable)
		}
	}

}
