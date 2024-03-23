package com.urosjarc.dbanalyser.gui.widgets.tables

import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.query.Query
import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.tableConnection.TableConnectionRepo
import com.urosjarc.dbanalyser.gui.parts.TableComboBox
import com.urosjarc.dbanalyser.gui.parts.TableConnectionTreeView
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class ConnectionSearchUi : KoinComponent {

	@FXML
	lateinit var startTableController: TableComboBox

	@FXML
	lateinit var endTableController: TableComboBox

	@FXML
	lateinit var tableTreeViewController: TableConnectionTreeView

	@FXML
	lateinit var searchB: Button

	@FXML
	lateinit var queryB: Button
}

class ConnectionSearch : ConnectionSearchUi() {
	val log = this.logger()
	val tableRepo by this.inject<TableRepo>()
	val queryRepo by this.inject<QueryRepo>()
	val clientService by this.inject<ClientService>()
	val tableConnectionRepo by this.inject<TableConnectionRepo>()

	@FXML
	fun initialize() {
		this.log.info(this.javaClass)
		this.searchB.setOnAction { this.search() }
		this.queryB.setOnAction { this.query() }
		this.tableConnectionRepo.onChose {
			this.startTableController.select(table = it?.table)
			this.search()
		}
	}

	fun query() {
		this.tableTreeViewController.self.selectionModel.selectedItem?.value?.let {
			val sql = this.clientService.selectSql(endTableConnection = it, countRelations = false)
			val dbMessiahSql = this.clientService.selectDbMessiahSql(endTableConnection = it)
			val query = Query(name = it.toString(), type = Query.Type.SELECT, sql = sql, dbMessiahSql = dbMessiahSql)
			this.queryRepo.save(query)
		}
	}

	fun search() {
		val startTable = startTableController.table ?: this.tableRepo.chosen
		val endTable = endTableController.table
		tableTreeViewController.update(startTable = startTable, endTable = endTable)
		tableRepo.chose(startTable)
	}
}
