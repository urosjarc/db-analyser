package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import com.urosjarc.dbanalyser.gui.parts.TableTreeView
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableSearchUi : KoinComponent {
    @FXML
    lateinit var startTableController: TableComboBox

    @FXML
    lateinit var endTableController: TableComboBox

    @FXML
    lateinit var tableTreeViewController: TableTreeView

    @FXML
    lateinit var searchB: Button


}

class TableSearch : TableSearchUi() {

    val tableRepo by this.inject<TableRepo>()

    @FXML
    fun initialize() {
        this.searchB.setOnAction { this.search() }
    }

    fun search() {
        val startTable = this.startTableController.table ?: return
        val endTable = this.endTableController.table
        this.tableTreeViewController.update(startTable=startTable, endTable=endTable)
        this.tableRepo.select(startTable)
    }

}
