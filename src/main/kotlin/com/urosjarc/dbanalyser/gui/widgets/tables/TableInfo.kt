package com.urosjarc.dbanalyser.gui.widgets.tables

import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.query.Query
import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.tableConnection.TableConnection
import com.urosjarc.dbanalyser.app.tableConnection.TableConnectionRepo
import com.urosjarc.dbanalyser.gui.events.ShowConnectionsEvent
import com.urosjarc.dbanalyser.gui.parts.ChildConnectionTableView
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import com.urosjarc.dbanalyser.gui.parts.ParentConnectionTableView
import com.urosjarc.dbanalyser.shared.setCopy
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class TableInfoUi : KoinComponent {
	@FXML
	lateinit var redoB: Button

	@FXML
	lateinit var nameL: Label

	@FXML
	lateinit var undoB: Button

	@FXML
	lateinit var columnTableViewController: ColumnTableView

	@FXML
	lateinit var childConnectionViewController: ChildConnectionTableView

	@FXML
	lateinit var parentConnectionViewController: ParentConnectionTableView

	@FXML
	lateinit var connectionsB: Button

	@FXML
	lateinit var insertMI: MenuItem

	@FXML
	lateinit var selectMI: MenuItem
}

class TableInfo : TableInfoUi() {
	val log = this.logger()
	val tableRepo by this.inject<TableRepo>()
	val tableConnectionRepo by this.inject<TableConnectionRepo>()
	val clientService by this.inject<ClientService>()
	val queryRepo by this.inject<QueryRepo>()
	val showConnectionsEvent by this.inject<ShowConnectionsEvent>()

	@FXML
	fun initialize() {
		this.log.info(this.javaClass)
		setCopy(label = this.nameL)
		this.redoB.setOnAction { this.redo() }
		this.undoB.setOnAction { this.undo() }
		this.insertMI.setOnAction { this.query(type = Query.Type.INSERT) }
		this.selectMI.setOnAction { this.query(type = Query.Type.SELECT) }
		this.connectionsB.setOnAction { this.connection() }

		this.columnTableViewController.also {
			it.tableTC.isVisible = false
			it.schemaTC.isVisible = false
			it.forwardOnForeignColumnClick = true
			it.forwardOnNumberClicks = 2
		}

		this.columnTableViewController.let { ctrl ->
			ctrl.tableRepo.onChose {
				this.nameL.text = it?.toString() ?: ""
				this.update()
				ctrl.update(it)
			}
		}
		this.update()
	}

	fun connection() {
		this.tableRepo.chosen?.let {
			val con = TableConnection(
				isParent = false,
				table = it,
				foreignKey = null
			)
			this.tableConnectionRepo.chose(con)
			this.showConnectionsEvent.chose()
		}
	}

	fun redo() {
		this.tableRepo.redo()
		this.update()
	}

	fun undo() {
		this.tableRepo.undo()
		this.update()
	}

	fun query(type: Query.Type) {
		this.tableRepo.chosen?.let {
			val query = Query(
				name = it.name,
				type = type,
				sql = when (type) {
					Query.Type.SELECT -> {
						val con = TableConnection(isParent = true, table = it, foreignKey = null)
						this.clientService.selectSql(endTableConnection = con, countRelations = false)
					}

					Query.Type.INSERT -> this.clientService.insertSql(table = it)
					else -> return
				}
			)
			this.queryRepo.save(query)
		}
	}

	fun update() {
		this.redoB.text = this.tableRepo.future.firstOrNull()?.toString() ?: "(empty)"
		this.undoB.text = this.tableRepo.history.lastOrNull()?.toString() ?: "(empty)"
	}
}
