package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.gui.elements.DbListView
import com.urosjarc.dbanalyser.gui.parts.DbLoginPart
import javafx.fxml.FXML
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbWidgetUi : KoinComponent {
    @FXML
    lateinit var dbLoginPartController: DbLoginPart

    @FXML
    lateinit var dbListViewController: DbListView

}

class DbWidget : DbWidgetUi() {
    val dbRepo by this.inject<DbRepo>()

    @FXML
    fun initialize() {
        this.dbRepo.watch { this.dbListViewController.init() }
    }
}
