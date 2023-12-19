package com.urosjarc.dbanalyser.gui.widgets.search

import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import com.urosjarc.dbanalyser.gui.parts.TableTreeView
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
	lateinit var tableTreeViewController: TableTreeView

	@FXML
	lateinit var searchB: Button

	@FXML
	lateinit var connectionTA: TextArea
}

class ConnectionSearch : ConnectionSearchUi() {

	val tableRepo by this.inject<TableRepo>()

	@FXML
	fun initialize() {
		this.searchB.setOnAction { this.search() }
		this.tableTreeViewController.self.selectionModel.selectedItemProperty().addListener { _, _, newValue -> this.chose(newValue.value) }
	}

	fun search() = startThread {
		Platform.runLater {
			val startTable = startTableController.table ?: return@runLater
			val endTable = endTableController.table
			tableTreeViewController.update(startTable = startTable, endTable = endTable)
			tableRepo.chose(startTable)
		}
	}

	fun chose(tableConnection: TableConnection) {
		var node = tableConnection
		val signs = mutableSetOf<String>()

		val joins = mutableListOf<String>()
		while (node.parent != null) {

			val fkey = node.foreignKey
			val start = if (node.isParent) fkey?.from else fkey?.to
			val end = if (node.isParent) fkey?.to else fkey?.from

			val startTable = start?.table
			var startSign = startTable?.name?.filter { it.isUpperCase() }!!
			val startColumn = start.name

			val endTable = end?.table
			val endSign = endTable?.name?.filter { it.isUpperCase() }
			val endColumn = end?.name

			joins.add(0, "\tJOIN ${startTable} ${startSign} ON ${startSign}.${startColumn} = ${endSign}.${endColumn}")

			var i = 1
			val originalStartSign = startSign
			while (signs.contains(startSign)) startSign = "$originalStartSign${i++}"
			signs.add(startSign)

			node = node.parent!!
		}

		val select = mutableListOf("SELECT")
		select.addAll(node.table.columns.map { "\t${it.name}" })
		select.add("FROM ${node.table} ${node.table.name.filter { it.isUpperCase() }}")

		this.connectionTA.text = (select + joins).joinToString(separator = "\n")
	}

}
