package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.gui.widgets.DbLogin
import com.urosjarc.dbanalyser.gui.widgets.DbSearch
import com.urosjarc.dbanalyser.gui.widgets.TableInfo
import javafx.fxml.FXML
import org.koin.core.component.KoinComponent

abstract class DbAnalyserUi {
    @FXML
    lateinit var dbLoginController: DbLogin
    @FXML
    lateinit var dbSearchController: DbSearch
    @FXML
    lateinit var tableInfoController: TableInfo
}

class DbAnalyser : DbAnalyserUi() {
    @FXML
    fun initialize() {
    }
}
