package com.urosjarc.dbanalyser.gui.widgets.queries

import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.query.Query
import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.shared.startThread
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class QueryInfoUi : KoinComponent {
	@FXML
	lateinit var sqlTA: TextArea

	@FXML
	lateinit var nameTF: TextField

	@FXML
	lateinit var executeB: Button

	@FXML
	lateinit var resultTA: TextArea

	@FXML
	lateinit var deleteB: Button

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var typeCB: ComboBox<Query.Type>
}

class QueryInfo : QueryInfoUi() {

	val queryRepo by this.inject<QueryRepo>()
	val clientService by this.inject<ClientService>()

	@FXML
	fun initialize() {
		this.queryRepo.onChose { this.update(it) }
		this.typeCB.items.setAll(Query.Type.values().toMutableList())
		this.executeB.setOnAction { this.execute() }
		this.saveB.setOnAction { this.save() }
		this.deleteB.setOnAction { this.delete() }
	}

	private fun update(query: Query) {
		this.typeCB.value = query.type
		this.nameTF.text = query.name
		this.sqlTA.text = query.sql
	}

	private fun delete() {
		val name = this.nameTF.text
		this.queryRepo.delete(name)
	}

	private fun save() {
		val query = Query(
			name = this.nameTF.text,
			type = this.typeCB.value,
			sql = this.sqlTA.text
		)
		this.queryRepo.save(query)
	}

	private fun execute() = startThread {
		val results = this.clientService.execute(sql = this.sqlTA.text)
		this.resultTA.text = results
	}

}
