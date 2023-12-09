package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.gui.parts.BackConnectionTableView
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.koin.core.component.KoinComponent


open class TableInfoUi : KoinComponent {
    @FXML
    lateinit var nameL: Label

    @FXML
    lateinit var columnTableViewController: ColumnTableView

    @FXML
    lateinit var backConnectionViewController: BackConnectionTableView

}

class TableInfo : TableInfoUi() {

    @FXML
    fun initialize(){
        this.columnTableViewController.let {ctrl ->
            ctrl.tableRepo.onSelect {
                this.nameL.text = it.name
                ctrl.update(it)
            }
        }
    }
}
