package com.urosjarc.dbanalyser.gui.widgets.search

import com.urosjarc.dbanalyser.gui.parts.ChildConnectionTableView
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import com.urosjarc.dbanalyser.gui.parts.ParentConnectionTableView
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.koin.core.component.KoinComponent


open class TableInfoUi : KoinComponent {
    @FXML
    lateinit var nameL: Label

    @FXML
    lateinit var columnTableViewController: ColumnTableView

    @FXML
    lateinit var childConnectionViewController: ChildConnectionTableView

    @FXML
    lateinit var parentConnectionViewController: ParentConnectionTableView

}

class TableInfo : TableInfoUi() {

    @FXML
    fun initialize(){
        this.columnTableViewController.also {
            it.tableTC.isVisible = false
            it.schemaTC.isVisible = false
            it.forwardOnForeignColumnClick = true
            it.forwardOnNumberClicks = 2
        }

        this.columnTableViewController.let {ctrl ->
            ctrl.tableRepo.onChose {
                this.nameL.text = it.toString()
                ctrl.update(it)
            }
        }
    }
}
