package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TreeTableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbSearchUi : KoinComponent {
    @FXML
    lateinit var startTableCB: ComboBox<Table>

    @FXML
    lateinit var endTableCB: ComboBox<Table>

    @FXML
    lateinit var searchB: Button

    @FXML
    lateinit var tableTTV: TreeTableView<Table>
}

class DbSearch : DbSearchUi() {
    val dbRepo by this.inject<DbRepo>()
    val tableRepo by this.inject<TableRepo>()

    @FXML
    fun initialize() {
        this.dbRepo.onSelect {
            println("Search: ${this.dbRepo.data}")
        }
        this.tableRepo.onChange {
            this.startTableCB.items.setAll(this.tableRepo.data)
            this.endTableCB.items.setAll(this.tableRepo.data)
        }
    }
}
