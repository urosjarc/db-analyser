package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.shared.matchRatio
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.input.KeyEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TableComboBoxUi : KoinComponent {
    @FXML
    lateinit var self: ComboBox<String?>
}

class TableComboBox : TableComboBoxUi() {
    val tableRepo by this.inject<TableRepo>()
    var table: Table? = null

    @FXML
    private fun initialize() {
        this.self.editor.textProperty().addListener { _, _, newText -> this.self.setValue(newText) }
        this.self.setOnKeyPressed { this.onKeyPressed(keyEvent = it) }
        this.self.valueProperty().addListener { _, _, newValue -> this.onSelect(newValue) }
        this.self.editor.setOnKeyPressed { this.onKeyPressed(keyEvent = it) }
        this.tableRepo.onChange { this.self.items.setAll(this.tableRepo.data.map { it.toString() }) }
        this.self.value = ""
    }

    private fun onKeyPressed(keyEvent: KeyEvent) {
        if (keyEvent.text.firstOrNull()?.isLetterOrDigit() == true) this.onChange()
    }

    fun onSelect(text: String?){
        this.table = this.tableRepo.data.firstOrNull { it.toString() == text }
        if(this.table != null) this.self.value = this.table.toString()
    }

    private fun onChange() {
        this.self.show()
        val tables = this.sortedTables()
        this.self.items = FXCollections.observableList(tables.map { it.toString() })
        this.table = tables.firstOrNull()
    }

    private fun sortedTables() = this.tableRepo.data.sortedByDescending { matchRatio(it.toString(), this.self.value ?: "") }

}
