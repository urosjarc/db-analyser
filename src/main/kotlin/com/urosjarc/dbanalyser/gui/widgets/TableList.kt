package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableListUi : KoinComponent {
    lateinit var self: TableView<Table>

    lateinit var connectionsTC: TableColumn<Table, Int>
    lateinit var allTC: TableColumn<Table, Int>
    lateinit var forwardTC: TableColumn<Table, Int>
    lateinit var backwardTC: TableColumn<Table, Int>
    lateinit var rowsTC: TableColumn<Table, Int>
    lateinit var columnsTC: TableColumn<Table, Int>
    lateinit var nameTC: TableColumn<Table, String>
}

class TableNotes : TableListUi() {


    val tableRepo by this.inject<TableRepo>()
    val tableService by this.inject<TableService>()

    @FXML
    fun initialize() {
        this.tableRepo.onChange { this.self.items.setAll(this.tableRepo.data) }
        this.self.setOnMouseClicked { this.onItemClicked(it) }

        this.nameTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }
        this.columnsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.columns.size) }
        this.forwardTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.foreignKeys.size) }
        this.rowsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.rows) }
        this.backwardTC.setCellValueFactory { ReadOnlyObjectWrapper(this.tableService.backwardConnections(table=it.value).size) }
        this.allTC.setCellValueFactory { ReadOnlyObjectWrapper(this.tableService.connections(table=it.value).size) }

        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        this.connectionsTC.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.allTC.maxWidth = (Integer.MAX_VALUE * 33.0)
        this.forwardTC.maxWidth = (Integer.MAX_VALUE * 33.0)
        this.backwardTC.maxWidth = (Integer.MAX_VALUE * 33.0)
        this.rowsTC.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.columnsTC.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.nameTC.maxWidth = (Integer.MAX_VALUE * 25.0)
    }

    fun onItemClicked(mouseEvent: MouseEvent) {
        if (mouseEvent.clickCount == 2) {
            val table = this.self.selectionModel.selectedItem
            this.tableRepo.select(table)
        }
    }

}
