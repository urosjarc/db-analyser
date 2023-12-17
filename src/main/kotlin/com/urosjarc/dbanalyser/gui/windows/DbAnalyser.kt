package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.app.schema.Schema
import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import com.urosjarc.dbanalyser.gui.widgets.tables.ConnectionSearch
import com.urosjarc.dbanalyser.gui.widgets.dbs.DbLogin
import com.urosjarc.dbanalyser.gui.widgets.tables.TableInfo
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
	lateinit var mainSchemasT: Tab

	@FXML
	lateinit var mainTablesT: Tab

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
		this.schemaRepo.onSelect { this.update(it) }
		this.schemaRepo.onData { this.update(it) }
	}

	fun update(schemas: List<Schema>) = startUiThread {
		this.tablesT.text = "Tables: ${schemas.sumOf { it.tables.size }}"
		this.columnsT.text = "Columns: ${schemas.sumOf { sch -> sch.tables.sumOf { it.columns.size } }}"
	}
}
