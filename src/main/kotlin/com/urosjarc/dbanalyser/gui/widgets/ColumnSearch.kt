package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.table.TableRepo
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.layout.FlowPane
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ColumnSearchUi : KoinComponent {
    @FXML
    lateinit var typeFP: FlowPane

    @FXML
    lateinit var searchB: Button

    @FXML
    lateinit var nameTF: TextField

    @FXML
    lateinit var constrainedCB: CheckBox

    @FXML
    lateinit var defaultCB: CheckBox

    @FXML
    lateinit var notNullCB: CheckBox

    @FXML
    lateinit var uniqueCB: CheckBox

    @FXML
    lateinit var selfRefKeyCB: CheckBox

    @FXML
    lateinit var foreignKeyCB: CheckBox

    @FXML
    lateinit var primaryKeyCB: CheckBox
}

class ColumnSearch : ColumnSearchUi() {

    val tableRepo by this.inject<TableRepo>()

    @FXML
    fun initialize() {
        this.tableRepo.onChange { this.update() }
        this.nameTF.setOnAction { this.search() }
        this.constrainedCB.setOnAction { this.search() }
        this.defaultCB.setOnAction { this.search() }
        this.notNullCB.setOnAction { this.search() }
        this.uniqueCB.setOnAction { this.search() }
        this.selfRefKeyCB.setOnAction { this.search() }
        this.foreignKeyCB.setOnAction { this.search() }
        this.primaryKeyCB.setOnAction { this.search() }
    }

    private fun search() {
        println("search")
    }

    fun update() {
        val baseTypes = mutableSetOf<String>()
        this.tableRepo.data.forEach { table ->
            table.columns.forEach { column: Column ->
                if (column.baseType.isNotBlank()) baseTypes.add(column.baseType)
            }
        }
        this.typeFP.children.setAll(baseTypes.map {
            CheckBox(it).also {
                it.isIndeterminate = true
                it.isAllowIndeterminate = true
            }
        })
    }
}
