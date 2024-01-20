package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.shared.matchRatio
import com.urosjarc.dbanalyser.shared.startThread
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.input.KeyEvent
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TableComboBoxUi : KoinComponent {
	@FXML
	lateinit var self: ComboBox<String?>
}

class TableComboBox : TableComboBoxUi() {
	val log = this.logger()
	val tableRepo by this.inject<TableRepo>()
	var table: Table? = null
	var searchThread: Thread? = null

	@FXML
	private fun initialize() {
		this.log.info(this.javaClass)
		this.self.editor.textProperty().addListener { _, _, newText -> this.self.setValue(newText) }
		this.self.setOnKeyPressed { this.onKeyPressed(keyEvent = it) }
		this.self.valueProperty().addListener { _, _, newValue -> this.select(newValue) }
		this.self.editor.setOnKeyPressed { this.onKeyPressed(keyEvent = it) }
		this.tableRepo.onData { this.self.items.setAll(this.tableRepo.data.map { it.toString() }) }
		this.self.value = ""
	}

	private fun onKeyPressed(keyEvent: KeyEvent) {
		if (keyEvent.text.firstOrNull()?.isLetterOrDigit() == true) this.search()
	}

	fun select(table: Table?) {
		this.table = table
		this.self.value = table?.toString() ?: ""
	}

	fun select(text: String?) {
		val table = this.tableRepo.data.firstOrNull { it.toString() == text }
		if (table != null) this.select(table = table)
	}

	private fun search() {
		this.self.show()
		this.searchThread?.interrupt()
		this.searchThread = startThread(sleep = 300) {
			val tables = this.tableRepo.data.sortedByDescending { matchRatio(it.name, this.self.value ?: "") }
			this.table = tables.firstOrNull()
			val sortedTableNames = FXCollections.observableList(tables.map { it.toString() })
			Platform.runLater { this.self.items = sortedTableNames } //SAVE!!!
		}

	}
}
