package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ColumnTableViewUi : KoinComponent {
    @FXML
    lateinit var self: TableView<Column>

    @FXML
    lateinit var toTC: TableColumn<Column, String>

    @FXML
    lateinit var typeTC: TableColumn<Column, String>

    @FXML
    lateinit var tableTC: TableColumn<Column, String>

    @FXML
    lateinit var nameTC: TableColumn<Column, String>

    @FXML
    lateinit var keyTC: TableColumn<Column, String>
}

class ColumnTableView : ColumnTableViewUi() {
    val tableRepo by this.inject<TableRepo>()

    var forwardOnNumberClicks = 0
    var forwardOnForeignColumnClick = false
    var forwardOnColumnClick = false

    @FXML
    fun initialize() {
        this.self.setOnMouseClicked { this.onItemClicked(it) }

        this.keyTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.meta) }
        this.typeTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.type) }
        this.tableTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.table) }
        this.nameTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }
        this.toTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.connection) }

        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.keyTC.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.typeTC.maxWidth = (Integer.MAX_VALUE * 20.0)
        this.tableTC.maxWidth = (Integer.MAX_VALUE * 20.0)
        this.nameTC.maxWidth = (Integer.MAX_VALUE * 30.0)
        this.toTC.maxWidth = (Integer.MAX_VALUE * 20.0)
    }

    fun update(table: Table) {
        this.self.items.setAll(table.columns)
    }

    fun onItemClicked(mouseEvent: MouseEvent) {
        if (mouseEvent.clickCount == this.forwardOnNumberClicks) {
            val column = this.self.selectionModel.selectedItem
            var table: Table? = null
            if (this.forwardOnColumnClick) table = this.tableRepo.find(column.table)
            if (this.forwardOnForeignColumnClick && column.foreignKey != null) {
                table = this.tableRepo.find(column.foreignKey.tableName)
            }
            if (table != null) this.tableRepo.select(table)

        }
    }
}
