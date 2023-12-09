package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ColumnTableViewUi : KoinComponent {
    @FXML
    lateinit var tableNameL: Label

    @FXML
    lateinit var self: TableView<Column>

    @FXML
    lateinit var forwardColumn: TableColumn<Column, String>

    @FXML
    lateinit var typeColumn: TableColumn<Column, String>

    @FXML
    lateinit var nameColumn: TableColumn<Column, String>

    @FXML
    lateinit var keyColumn: TableColumn<Column, String>
}
class ColumnTableView : ColumnTableViewUi() {
    val tableRepo by this.inject<TableRepo>()
    @FXML
    fun initialize(){
        this.tableRepo.onSelect { this.update(it) }

        this.keyColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.meta) }
        this.typeColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.type) }
        this.nameColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }
        this.forwardColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.connection) }

        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.keyColumn.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.typeColumn.maxWidth = (Integer.MAX_VALUE * 30.0)
        this.nameColumn.maxWidth = (Integer.MAX_VALUE * 30.0)
        this.forwardColumn.maxWidth = (Integer.MAX_VALUE * 30.0)
    }

    fun update(table: Table) {
        this.self.items.setAll(table.columns)
        this.tableNameL.text = table.name
    }
}
