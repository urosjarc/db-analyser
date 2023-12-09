package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import com.urosjarc.dbanalyser.gui.parts.TableTreeView
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class DbSearchUi : KoinComponent {
    @FXML
    lateinit var startTableController: TableComboBox

    @FXML
    lateinit var endTableController: TableComboBox

    @FXML
    lateinit var tableTreeViewController: TableTreeView

    @FXML
    lateinit var searchB: Button
}

class DbSearch : DbSearchUi() {

    val tableRepo by this.inject<TableRepo>()

    @FXML
    fun initialize() {
        this.searchB.setOnAction { this.search() }
    }

    fun search() {
        val startTable = this.startTableController.table!!
        val endTable = this.endTableController.table
        this.tableTreeViewController.update(startTable=startTable, endTable=endTable)
        this.tableRepo.select(startTable)
    }

}
