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
    lateinit var dataColumn: TableColumn<Log, String>

    @FXML
    lateinit var timeColumn: TableColumn<Log, String>

    @FXML
    lateinit var levelColumn: TableColumn<Log, String>

    @FXML
    lateinit var logsTV: TableView<Log>
}

class AppLogs : AppLogsUi() {

    val logRepo by this.inject<LogRepo>()

    @FXML
    fun initialize() {
        this.logsTV.items.addAll(this.logRepo.data)
        this.logRepo.onSelect { this.logsTV.items.add(it) }

        this.levelColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.type.name) }
        this.timeColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.time.toString()) }
        this.dataColumn.setCellValueFactory { ReadOnlyStringWrapper(it.value.data) }

        this.logsTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        this.levelColumn.maxWidth = (1f * Integer.MAX_VALUE * 20).toDouble()
        this.timeColumn.maxWidth = (1f * Integer.MAX_VALUE * 20).toDouble()
        this.dataColumn.maxWidth = (1f * Integer.MAX_VALUE * 60).toDouble()
    }

}
