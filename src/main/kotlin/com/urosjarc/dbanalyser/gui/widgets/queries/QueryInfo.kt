package com.urosjarc.dbanalyser.gui.widgets.queries

import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.query.Query
import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.app.query.QueryResult
import javafx.fxml.FXML
import javafx.scene.control.*
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

	@FXML
	lateinit var dtoTA: TextArea

	@FXML
	lateinit var langCB: ChoiceBox<QueryResult.Header.Lang>
}

class QueryInfo : QueryInfoUi() {

	val queryRepo by this.inject<QueryRepo>()
	val clientService by this.inject<ClientService>()

	@FXML
	fun initialize() {
		this.langCB.items.setAll(QueryResult.Header.Lang.values().toMutableList())
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

	private fun execute() {
		val results = this.clientService.execute(sql = this.sqlTA.text)
		this.resultTA.text = results.map { it.data }.joinToString("\n")
		val lang: QueryResult.Header.Lang = this.langCB.value ?: QueryResult.Header.Lang.KOTLIN
		this.dtoTA.text = results.map {
			when (lang) {
				QueryResult.Header.Lang.JAVA -> it.javaDTO()
				QueryResult.Header.Lang.TYPESCRIPT -> it.typescriptDTO()
				QueryResult.Header.Lang.KOTLIN -> it.kotlinDTO()
			}
		}.joinToString("\n\n")
	}

}
