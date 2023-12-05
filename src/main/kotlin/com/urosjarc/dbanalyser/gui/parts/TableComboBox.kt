package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class TableComboBoxUi : KoinComponent {
    @FXML
    lateinit var self: ComboBox<Table>
}

class TableComboBox : TableComboBoxUi() {
    val tableRepo by this.inject<TableRepo>()
    var table: Table? = null

    @FXML
    private fun initialize() {
        this.self.editor.setOnKeyPressed { this.onKeyPressed(keyEvent = it) }
        this.self.setOnKeyPressed { this.onKeyPressed(keyEvent = it) }
        this.tableRepo.onChange { this.self.items.setAll(this.tableRepo.data) }
    }

    private fun onKeyPressed(keyEvent: KeyEvent) {
        if (keyEvent.text.firstOrNull()?.isLetterOrDigit() == true) this.onChange()
        if (keyEvent.code == KeyCode.TAB) this.onTab()
    }

    private fun onChange() {
        this.self.show()
        this.table = null
        val tables = this.tableRepo.data.sortedByDescending { FuzzySearch.ratio(it.name, this.self.editor.text) }
        this.self.items = FXCollections.observableList(tables)
    }

    private fun onTab() {
        this.table = this.self.items.first()
        this.self.value = this.table
    }

}
