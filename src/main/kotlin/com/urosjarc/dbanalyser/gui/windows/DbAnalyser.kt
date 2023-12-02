package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.gui.widgets.DbLogin
import com.urosjarc.dbanalyser.gui.widgets.DbSearch
import javafx.fxml.FXML

abstract class DbAnalyserUi {
    @FXML
    lateinit var dbLoginController: DbLogin
    @FXML
    lateinit var dbSearchController: DbSearch
}

class DbAnalyser : DbAnalyserUi() {
    @FXML
    fun initialize() {
    }
}
