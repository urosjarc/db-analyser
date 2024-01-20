package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.query.Query
import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.shared.matchRatio
import com.urosjarc.dbanalyser.shared.setColumnWidth
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class QueryTableViewUi : KoinComponent {
	@FXML
	lateinit var queryTF: TextField

	@FXML
	lateinit var queryTV: TableView<Query>

	@FXML
	lateinit var typeTC: TableColumn<Query, String>

	@FXML
	lateinit var nameTC: TableColumn<Query, String>
}

class QueryTableView : QueryTableViewUi() {
	val log = this.logger()
	val queryRepo by this.inject<QueryRepo>()

	@FXML
	fun initialize() {
		this.log.info(this.javaClass)
		this.queryRepo.onData { this.update(it) }
		this.queryTV.setOnMousePressed { this.chose(it) }
		this.queryTF.setOnAction { this.search() }

		this.typeTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.type.name) }
		this.nameTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.name) }

		this.queryTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
		setColumnWidth(this.typeTC, 25)
		setColumnWidth(this.nameTC, 75)
	}

	fun update(queries: List<Query>) {
		this.queryTV.items.setAll(queries)
	}

	private fun search() {
		this.queryTV.items.sortByDescending { matchRatio(it.name, this.queryTF.text) }
	}

	fun chose(mouseEvent: MouseEvent) {
		val query: Query = this.queryTV.selectionModel.selectedItem ?: return
		if (mouseEvent.isPrimaryButtonDown) this.queryRepo.chose(query)
		if (mouseEvent.isSecondaryButtonDown) this.queryRepo.delete(query)
	}
}
