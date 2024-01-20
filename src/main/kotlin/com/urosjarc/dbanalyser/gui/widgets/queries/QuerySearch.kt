package com.urosjarc.dbanalyser.gui.widgets.queries

import javafx.fxml.FXML
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent


open class QuerySearchUi : KoinComponent

class QuerySearch : QuerySearchUi() {

	val log = this.logger()
	@FXML
	fun initialize(){
		this.log.info(this.javaClass)
	}
}
