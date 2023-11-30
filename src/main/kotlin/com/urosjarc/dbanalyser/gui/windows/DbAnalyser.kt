package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.gui.widgets.DbWidget
import javafx.fxml.FXML

abstract class DbAnalyserUi {
    @FXML
    lateinit var dbWidgetController: DbWidget

}

class DbAnalyser : DbAnalyserUi() {
    @FXML
    fun initialize() {
        println("init DbAnalyser ${this.dbWidgetController}");

    }
}
