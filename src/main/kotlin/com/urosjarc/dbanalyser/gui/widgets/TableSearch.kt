package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableSearchUi : KoinComponent {
    @FXML
    lateinit var self: TableView<Table>

    @FXML
    lateinit var relativesTC: TableColumn<Table, Int>

    @FXML
    lateinit var parentsTC: TableColumn<Table, Int>

    @FXML
    lateinit var childrenTC: TableColumn<Table, Int>

    @FXML
    lateinit var rowsTC: TableColumn<Table, Int>

    @FXML
    lateinit var columnsTC: TableColumn<Table, Int>

    @FXML
    lateinit var nameTC: TableColumn<Table, String>

    @FXML
    lateinit var tableTF: TextField
}

class TableSearch : TableSearchUi() {
    val tableRepo by this.inject<TableRepo>()
    val tableService by this.inject<TableService>()

    @FXML
    fun initialize() {
        this.tableRepo.onChange { this.self.items.setAll(this.tableRepo.data) }
        this.self.setOnMouseClicked { this.onItemClicked(it) }

        this.nameTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }
        this.columnsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.columns.size) }
        this.parentsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.foreignKeys.size) }
        this.rowsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.rows) }
        this.childrenTC.setCellValueFactory { ReadOnlyObjectWrapper(this.tableService.backwardConnections(table = it.value).size) }
        this.relativesTC.setCellValueFactory { ReadOnlyObjectWrapper(this.tableService.connections(table = it.value).size) }

        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        this.relativesTC.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.parentsTC.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.childrenTC.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.rowsTC.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.columnsTC.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.nameTC.maxWidth = (Integer.MAX_VALUE * 50.0)
    }

    fun onItemClicked(mouseEvent: MouseEvent) {
        if (mouseEvent.clickCount == 2) {
            val table = this.self.selectionModel.selectedItem
            this.tableRepo.select(table)
        }
    }

}
