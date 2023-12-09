package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.gui.widgets.DbLogin
import com.urosjarc.dbanalyser.gui.widgets.TableSearch
import com.urosjarc.dbanalyser.gui.widgets.TableInfo
import javafx.fxml.FXML

abstract class DbAnalyserUi {
    @FXML
    lateinit var dbLoginController: DbLogin
    @FXML
    lateinit var tableSearchController: TableSearch
    @FXML
    lateinit var tableInfoController: TableInfo
}

class DbAnalyser : DbAnalyserUi() {
    @FXML
    fun initialize() {
    }
}
