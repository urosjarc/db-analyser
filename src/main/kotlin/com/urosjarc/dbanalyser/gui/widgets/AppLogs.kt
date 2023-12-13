package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.logs.Log
import com.urosjarc.dbanalyser.app.logs.LogRepo
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class AppLogsUi : KoinComponent {
    @FXML
    lateinit var logsTV: TableView<Log>

    @FXML
    lateinit var dataTC: TableColumn<Log, String>

    @FXML
    lateinit var timeTC: TableColumn<Log, String>

    @FXML
    lateinit var levelTC: TableColumn<Log, String>

}

class AppLogs : AppLogsUi() {

    val logRepo by this.inject<LogRepo>()

    @FXML
    fun initialize() {
        this.logsTV.items.addAll(this.logRepo.data)
        this.logRepo.onSelect { this.logsTV.items.add(it) }

        this.levelTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.type.name) }
        this.timeTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.time.toString()) }
        this.dataTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.data) }

        this.logsTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.levelTC.maxWidth = (1f * Integer.MAX_VALUE * 20).toDouble()
        this.timeTC.maxWidth = (1f * Integer.MAX_VALUE * 20).toDouble()
        this.dataTC.maxWidth = (1f * Integer.MAX_VALUE * 60).toDouble()
    }

}
