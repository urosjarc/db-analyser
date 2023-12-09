package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.gui.parts.BackConnectionTableView
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import javafx.fxml.FXML
import org.koin.core.component.KoinComponent


open class TableInfoUi : KoinComponent {

    @FXML
    lateinit var columnTableViewController: ColumnTableView

    @FXML
    lateinit var backConnectionViewController: BackConnectionTableView
}

class TableInfo : TableInfoUi()
