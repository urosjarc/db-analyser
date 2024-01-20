package com.urosjarc.dbanalyser.gui.windows

import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.app.schema.Schema
import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.events.ShowConnectionsEvent
import com.urosjarc.dbanalyser.shared.startThread
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbAnalyserUi : KoinComponent {
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
}

class DbAnalyser : DbAnalyserUi() {

	val log = this.logger()
	val schemaRepo by this.inject<SchemaRepo>()
	val queryRepo by this.inject<QueryRepo>()
	val tableRepo by this.inject<TableRepo>()
	val showConnectionsEvent by this.inject<ShowConnectionsEvent>()

	@FXML
	fun initialize() {
		this.log.info(this.javaClass)
		this.schemaRepo.onSelect { this.update(it) }
		this.schemaRepo.onData { this.update(it) }
		this.queryRepo.onData { this.queriesT.text = "Queries: ${it.size}" }
		this.showConnectionsEvent.onChose { this.tablesTP.selectionModel.select(this.connectionsT) }
	}

	fun update(schemas: List<Schema>) = startThread {
		this.tablesTP.selectionModel.select(this.tablesT)
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
