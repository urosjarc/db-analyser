package com.urosjarc.dbanalyser.gui.widgets.search

import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.gui.parts.ChildConnectionTableView
import com.urosjarc.dbanalyser.gui.parts.ColumnTableView
import com.urosjarc.dbanalyser.gui.parts.ParentConnectionTableView
import com.urosjarc.dbanalyser.shared.setCopy
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
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
}

class TableInfo : TableInfoUi() {

	val tableRepo by this.inject<TableRepo>()


	@FXML
	fun initialize() {
		this.columnTableViewController.also {
			it.tableTC.isVisible = false
			it.schemaTC.isVisible = false
			it.forwardOnForeignColumnClick = true
			it.forwardOnNumberClicks = 2
		}

		this.columnTableViewController.let { ctrl ->
			ctrl.tableRepo.onChose {
				this.nameL.text = it.toString()
				this.update()
				ctrl.update(it)
			}
		}

		setCopy(label = this.nameL)

		this.redoB.setOnAction {
			this.tableRepo.redo()
			this.update()
		}
		this.undoB.setOnAction {
			this.tableRepo.undo()
			this.update()
		}

		this.update()
	}

	fun update() {
		this.redoB.text = this.tableRepo.future.firstOrNull()?.toString() ?: "(empty)"
		this.undoB.text = this.tableRepo.history.lastOrNull()?.toString() ?: "(empty)"
	}
}
