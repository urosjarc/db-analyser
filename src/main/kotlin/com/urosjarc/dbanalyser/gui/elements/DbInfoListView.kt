package com.urosjarc.dbanalyser.gui.elements

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ListView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class DbInfoListViewUi : KoinComponent {
    @FXML
    lateinit var self: ListView<Db>
}

class DbInfoListView : DbInfoListViewUi() {

    val dbRepo by this.inject<DbRepo>()
    fun init() {
        this.self.items = FXCollections.observableArrayList(this.dbRepo.get())
    }
}
