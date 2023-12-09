package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableInfoUi : KoinComponent {
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

    @FXML
    lateinit var backwardTC: TableView<TableConnection>

    @FXML
    lateinit var toColumn: TableColumn<TableConnection, String>

    @FXML
    lateinit var toKeyColumn: TableColumn<TableConnection, String>

    @FXML
    lateinit var fromColumn: TableColumn<TableConnection, String>

    @FXML
    lateinit var fromKeyColumn: TableColumn<TableConnection, String>
}

class TableInfo : TableInfoUi() {
    val tableRepo by this.inject<TableRepo>()
    val tableService by this.inject<TableService>()

    @FXML
    fun initialize() {
        this.tableRepo.onSelect { this.update(it) }

        this.keyColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.meta) }
        this.typeColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.type) }
        this.nameColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }
        this.forwardColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.connection)}

        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.keyColumn.maxWidth = (Integer.MAX_VALUE * 10.0)
        this.typeColumn.maxWidth = (Integer.MAX_VALUE * 30.0)
        this.nameColumn.maxWidth = (Integer.MAX_VALUE * 30.0)
        this.forwardColumn.maxWidth = (Integer.MAX_VALUE * 30.0)

        this.fromColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.table.name) }
        this.fromKeyColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.from) }
        this.toColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.tableName) }
        this.toKeyColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.to) }

        this.backwardTC.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.fromColumn.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.toColumn.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.fromKeyColumn.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.toKeyColumn.maxWidth = (Integer.MAX_VALUE * 25.0)

    }

    fun update(table: Table) {
        this.self.items.setAll(table.columns)
        this.tableNameL.text = table.name
        this.backwardTC.items.setAll(this.tableService.backwardConnections(table=table))
    }

}
