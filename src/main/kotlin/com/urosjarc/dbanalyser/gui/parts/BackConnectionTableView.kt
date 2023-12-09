package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BackConnectionTableViewUi : KoinComponent {
    @FXML
    lateinit var self: TableView<TableConnection>

    @FXML
    lateinit var toColumn: TableColumn<TableConnection, String>

    @FXML
    lateinit var toKeyColumn: TableColumn<TableConnection, String>

    @FXML
    lateinit var fromColumn: TableColumn<TableConnection, String>

    @FXML
    lateinit var fromKeyColumn: TableColumn<TableConnection, String>
}

class BackConnectionTableView: BackConnectionTableViewUi() {
    val tableRepo by this.inject<TableRepo>()
    val tableService by this.inject<TableService>()
    @FXML
    fun initialize(){
        this.tableRepo.onSelect { this.update(it) }

        this.fromColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.table.name) }
        this.fromKeyColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.from) }
        this.toColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.tableName) }
        this.toKeyColumn.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.to) }

        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.fromColumn.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.toColumn.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.fromKeyColumn.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.toKeyColumn.maxWidth = (Integer.MAX_VALUE * 25.0)
    }

    fun update(table: Table) {
        this.self.items.setAll(this.tableService.backwardConnections(table=table))
    }
}
