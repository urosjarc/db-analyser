package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TreeTableView
import javafx.scene.input.KeyCode
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbSearchUi : KoinComponent {
    @FXML
    lateinit var startTableCB: ComboBox<String>

    @FXML
    lateinit var endTableCB: ComboBox<String>

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
        this.searchB.setOnAction { this.search() }

        this.endTableCB.editor.textProperty().addListener { _, _, newValue ->
            this.endTableCB.show()
            var tables = this.tableRepo.data.map { it.name }
            tables = tables.sortedByDescending { FuzzySearch.ratio(it, newValue) }
            this.endTableCB.items = FXCollections.observableList(tables)
        }

        this.startTableCB.editor.textProperty().addListener { _, _, newValue ->
            this.startTableCB.show()
            var tables = this.tableRepo.data.map { it.name }
            tables = tables.sortedByDescending { FuzzySearch.ratio(it, newValue) }
            this.startTableCB.items = FXCollections.observableList(tables)
        }

        this.startTableCB.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) this.startTableCB.value = this.startTableCB.items.first()
        }
        this.endTableCB.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) this.endTableCB.value = this.endTableCB.items.first()
        }

        this.tableRepo.onChange {
            this.tableRepo.data.forEach {
                this.startTableCB.items.setAll(this.tableRepo.data.map { it.toString() })
                this.endTableCB.items.setAll(this.tableRepo.data.map { it.toString() })
            }
        }
    }

    fun search() {
        val startName: String = this.startTableCB.value.toString()
        val endName: String = this.endTableCB.value.toString()

        val startTable = this.tableRepo.data.maxBy { FuzzySearch.ratio(it.name, startName) }
        val endTable = this.tableRepo.data.maxBy { FuzzySearch.ratio(it.name, endName) }

        this.startTableCB.value = startTable.name
        this.endTableCB.value = endTable.name

        println(startTable.name)
        startTable.columns.forEach { println(" - ${it.name} ${it.type}") }

        println(endTable.name)
        endTable.columns.forEach { println(" - ${it.name} ${it.type}") }
    }
}
