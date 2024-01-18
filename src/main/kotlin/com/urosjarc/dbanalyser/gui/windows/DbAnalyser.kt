package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.app.schema.Schema
import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import com.urosjarc.dbanalyser.gui.widgets.dbs.DbLogin
import com.urosjarc.dbanalyser.gui.widgets.queries.QuerySearch
import com.urosjarc.dbanalyser.gui.widgets.tables.ConnectionSearch
import com.urosjarc.dbanalyser.gui.widgets.tables.TableInfo
import com.urosjarc.dbanalyser.shared.startThread
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
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
	lateinit var querySearchController: QuerySearch

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

	@FXML
	lateinit var schemasT: Tab

	@FXML
	lateinit var queriesT: Tab

	@FXML
	lateinit var tablesParentT: Tab

	@FXML
	lateinit var dbT: Tab

	@FXML
	lateinit var tablesTP: TabPane

	@FXML
	lateinit var dtosT: Tab
}

class DbAnalyser : DbAnalyserUi() {

	val schemaRepo by this.inject<SchemaRepo>()
	val queryRepo by this.inject<QueryRepo>()
	val tableRepo by this.inject<TableRepo>()

	@FXML
	fun initialize() {
		this.schemaRepo.onSelect { this.update(it) }
		this.schemaRepo.onData { this.update(it) }
		this.queryRepo.onData { this.queriesT.text = "Queries: ${it.size}" }
		this.tableInfoController.connectionsB.setOnAction {
			this.tableInfoController.connection()
			this.tablesTP.selectionModel.select(this.connectionsT)
		}
	}

	fun update(schemas: List<Schema>) = startThread {
		val tableSize = schemas.sumOf { it.tables.size }
		val columnSize = schemas.sumOf { sch -> sch.tables.sumOf { it.columns.size } }
		Platform.runLater { //SAFE!!!
			this.schemasT.text = "Schemas: ${schemas.size}"
			this.tablesT.text = "Tables: $tableSize"
			this.tablesParentT.text = this.tablesT.text
			this.columnsT.text = "Columns: $columnSize"
		}
	}
}
