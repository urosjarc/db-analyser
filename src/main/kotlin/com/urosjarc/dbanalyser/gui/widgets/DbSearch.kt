package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TreeTableView
import org.koin.core.component.KoinComponent

abstract class DbSearchUi : KoinComponent {
    @FXML
    lateinit var startTableController: TableComboBox

    @FXML
    lateinit var endTableController: TableComboBox

    @FXML
    lateinit var searchB: Button

    @FXML
    lateinit var tableTTV: TreeTableView<Table>
}

class DbSearch : DbSearchUi() {

    @FXML
    fun initialize() {
        this.searchB.setOnAction { this.search() }
    }

    fun search() {
        val startTable = this.startTableController.table
        val endTable = this.endTableController.table

        println(startTable)
        startTable?.columns?.forEach { println(it) }

        println(endTable)
        endTable?.columns?.forEach { println(it) }
    }
}
