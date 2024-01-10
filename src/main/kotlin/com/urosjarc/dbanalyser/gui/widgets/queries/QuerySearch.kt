package com.urosjarc.dbanalyser.gui.widgets.queries

import com.urosjarc.dbanalyser.gui.parts.QueryTableView
import javafx.fxml.FXML
import org.koin.core.component.KoinComponent


open class QuerySearchUi : KoinComponent {
	@FXML
	lateinit var queryInfoController: QueryInfo

	@FXML
	lateinit var queryTableViewController: QueryTableView
}

class QuerySearch : QuerySearchUi() {
	@FXML
	fun initialize(){

	}
}
