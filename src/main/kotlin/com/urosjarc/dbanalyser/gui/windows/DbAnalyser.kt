package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import com.urosjarc.dbanalyser.gui.widgets.ConnectionSearch
import com.urosjarc.dbanalyser.gui.widgets.DbLogin
import com.urosjarc.dbanalyser.gui.widgets.TableInfo
import com.urosjarc.dbanalyser.shared.startThread
import com.urosjarc.dbanalyser.shared.startUiThread
import javafx.fxml.FXML
import javafx.scene.control.Tab
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbAnalyserUi : KoinComponent {
	@FXML
	lateinit var dbLoginController: DbLogin

	@FXML
	lateinit var connectionSearchController: ConnectionSearch

	@FXML
	lateinit var tableInfoController: TableInfo

	@FXML
	lateinit var columnTableViewController: ColumnTableView

	@FXML
	lateinit var schemasT: Tab

	@FXML
	lateinit var tablesT: Tab

	@FXML
	lateinit var columnsT: Tab

	@FXML
	lateinit var connectionsT: Tab
}

class DbAnalyser : DbAnalyserUi() {
	val schemaRepo by this.inject<SchemaRepo>()

	@FXML
	fun initialize() {
		this.schemaRepo.onChange { this.update() }
	}

	fun update() = startUiThread {
		this.schemasT.text = "Schemas: ${schemaRepo.data.size}"
		this.tablesT.text = "Tables: ${schemaRepo.data.sumOf { it.tables.size }}"
		this.columnsT.text = "Columns: ${schemaRepo.data.sumOf { sch -> sch.tables.sumOf { it.columns.size } }}"
	}
}
