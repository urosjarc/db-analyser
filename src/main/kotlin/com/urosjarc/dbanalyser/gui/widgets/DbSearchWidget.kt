package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.gui.elements.DbInfoListView
import com.urosjarc.dbanalyser.gui.parts.DbLoginPart
import javafx.fxml.FXML
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbSearchWidgetUi : KoinComponent {
    @FXML
    lateinit var dbLoginPartController: DbLoginPart

    @FXML
    lateinit var dbInfoListViewController: DbInfoListView

}

class DbSearchWidget : DbWidgetUi() {
    val dbRepo by this.inject<DbRepo>()

    @FXML
    fun initialize() {
        this.dbRepo.watch { this.dbInfoListViewController.init() }
    }
}
