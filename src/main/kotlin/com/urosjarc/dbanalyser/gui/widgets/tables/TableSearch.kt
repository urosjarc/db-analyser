package com.urosjarc.dbanalyser.gui.widgets.tables

import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import com.urosjarc.dbanalyser.shared.*
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.stage.FileChooser
import kotlinx.datetime.Clock
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableSearchUi : KoinComponent {
    class Model(
        val schemaTC: String,
        val tableTC: String,
        val columnsTC: Int,
        val parentsTC: Int,
        val childrenTC: Int,
    ) {
        val relativesTC: Int = this.parentsTC + this.childrenTC
    }

    @FXML
    lateinit var exportDbDiagramIoB: Button

    @FXML
    lateinit var exportPlantUmlB: Button

    @FXML
    lateinit var tableTF: TextField

    @FXML
    lateinit var modelTV: TableView<Model>

    @FXML
    lateinit var relativesTC: TableColumn<Model, Int>

    @FXML
    lateinit var parentsTC: TableColumn<Model, Int>

    @FXML
    lateinit var childrenTC: TableColumn<Model, Int>

    @FXML
    lateinit var columnsTC: TableColumn<Model, Int>

    @FXML
    lateinit var schemaTC: TableColumn<Model, String>

    @FXML
    lateinit var tableTC: TableColumn<Model, String>

}

class TableSearch : TableSearchUi() {
    val log = this.logger()
    val tableRepo by this.inject<TableRepo>()
    val schemaRepo by this.inject<SchemaRepo>()
    val tableService by this.inject<TableService>()

    @FXML
    fun initialize() {
        this.log.info(this.javaClass)
        this.tableRepo.onData { this.update() }
        this.modelTV.setOnMouseClicked { this.chose(it) }
        this.tableTF.setOnAction { this.search() }
        this.exportDbDiagramIoB.setOnAction { this.exportDbDiagramIo() }
        this.exportPlantUmlB.setOnAction { this.exportPlantUML() }

        this.schemaTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.schemaTC) }
        this.tableTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.tableTC) }
        this.columnsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.columnsTC) }
        this.parentsTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.parentsTC) }
        this.childrenTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.childrenTC) }
        this.relativesTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.relativesTC) }

        this.modelTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        setColumnWidth(this.relativesTC, 10)
        setColumnWidth(this.parentsTC, 10)
        setColumnWidth(this.childrenTC, 10)
        setColumnWidth(this.columnsTC, 10)
        setColumnWidth(this.schemaTC, 10)
        setColumnWidth(this.tableTC, 50)
    }

    private fun update() = startThread {
        this.modelTV.items.setAll(this.tableRepo.data.map {
            Model(
                schemaTC = it.schema.toString(),
                tableTC = it.name,
                columnsTC = it.columns.size,
                parentsTC = it.foreignKeys.size,
                childrenTC = this.tableService.children(table = it).size
            )
        })
    }

    private fun search() = startThread {
        modelTV.items.sortByDescending { matchRatio(it.tableTC, tableTF.text) }
    }

    private fun exportDbDiagramIo() {
        val instant = Clock.System.now()
        FileChooser().also {
            it.title = "Save dbdiagram.io export"
            it.initialFileName = "export_${instant.epochSeconds}.dbdiagramio"
            it.showSaveDialog(null)?.let { file ->
                if (!file.exists()) {
                    val export = dbdiagram_io(this.tableRepo.data)
                    file.writeText(export)
                }
            }
        }
    }

    private fun exportPlantUML() {
        val instant = Clock.System.now()
        FileChooser().also {
            it.title = "Save PlantUML export"
            it.initialFileName = "export_${instant.epochSeconds}.plantuml"
            it.showSaveDialog(null)?.let { file ->
                if (!file.exists()) {
                    val export = plantUML(this.schemaRepo.selected)
                    file.writeText(export)
                }
            }
        }
    }

    private fun chose(mouseEvent: MouseEvent) {
        if (mouseEvent.clickCount == 1) {
            val item = this.modelTV.selectionModel.selectedItem
            if (item != null) {
                val table: Table = this.tableRepo.find(item.tableTC)!!
                this.tableRepo.chose(table)
            }
        }
    }
}
