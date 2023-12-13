package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableConnection
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ForwardConnectionTableViewUi : KoinComponent {
    @FXML
    lateinit var self: TableView<TableConnection>

    @FXML
    lateinit var toTC: TableColumn<TableConnection, String>

    @FXML
    lateinit var toKeyTC: TableColumn<TableConnection, String>

    @FXML
    lateinit var fromTC: TableColumn<TableConnection, String>

    @FXML
    lateinit var fromKeyTC: TableColumn<TableConnection, String>
}

class ParentConnectionTableView: ForwardConnectionTableViewUi() {
    val tableRepo by this.inject<TableRepo>()
    val tableService by this.inject<TableService>()
    @FXML
    fun initialize(){
        this.tableRepo.onSelect { this.update(it) }
        this.self.setOnMouseClicked { this.onItemClicked(it) }

        this.fromTC.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.tableName) }
        this.fromKeyTC.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.to) }
        this.toTC.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.table.name) }
        this.toKeyTC.setCellValueFactory { val conn = it.value; ReadOnlyStringWrapper(conn.foreignKey?.from) }

        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.fromTC.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.toTC.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.fromKeyTC.maxWidth = (Integer.MAX_VALUE * 25.0)
        this.toKeyTC.maxWidth = (Integer.MAX_VALUE * 25.0)
    }

    private fun onItemClicked(mouseEvent: MouseEvent) {
        if(mouseEvent.clickCount == 2){
            val column = this.self.selectionModel.selectedItem
            this.tableRepo.select(column.table)
        }
    }

    fun update(table: Table) {
        this.self.items.setAll(this.tableService.forwardConnections(table=table))
    }
}
