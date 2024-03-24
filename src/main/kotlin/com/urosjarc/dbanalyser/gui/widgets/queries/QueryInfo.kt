package com.urosjarc.dbanalyser.gui.widgets.queries

import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.query.Query
import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.app.query.QueryResult
import javafx.fxml.FXML
import javafx.scene.control.*
import org.apache.logging.log4j.kotlin.logger
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
    lateinit var langCB: ChoiceBox<QueryResult.Lang>

    @FXML
    lateinit var dbMessiahSqlTA: TextArea
}

class QueryInfo : QueryInfoUi() {

    val log = this.logger()
    val dbRepo by this.inject<DbRepo>()
    val queryRepo by this.inject<QueryRepo>()
    val clientService by this.inject<ClientService>()
    var lastResults: MutableList<QueryResult> = mutableListOf()


    @FXML
    fun initialize() {
        this.log.info(this.javaClass)
        this.langCB.items.setAll(QueryResult.Lang.values().toMutableList())
        this.langCB.value = this.langCB.items.first()
        this.queryRepo.onChose { this.update(it) }
        this.typeCB.items.setAll(Query.Type.values().toMutableList())
        this.executeB.setOnAction { this.execute() }
        this.saveB.setOnAction { this.save() }
        this.deleteB.setOnAction { this.delete() }
        this.langCB.setOnAction { this.updateDTO() }

        val db = this.dbRepo.chosen?.type
        if (listOf(Db.Type.DB2, Db.Type.H2, Db.Type.MARIA, Db.Type.MYSQL, Db.Type.ORACLE).contains(db)) {
            this.resultTA.promptText = """
			SQL statement...
			
			WARNING:
				* Because of limitation of the $db driver,
				* you are allowed write only single query per window!
		""".trimIndent()
            this.dtoTA.promptText = this.resultTA.promptText
        }
    }

    private fun updateDTO() {
        val lang: QueryResult.Lang = this.langCB.value ?: return
        this.dtoTA.text = this.lastResults.map {
            when (lang) {
                QueryResult.Lang.JAVA -> it.javaDTO()
                QueryResult.Lang.TYPESCRIPT -> it.typescriptDTO()
                QueryResult.Lang.KOTLIN -> it.kotlinDTO()
            }
        }.joinToString("\n\n")
    }
    private fun update(query: Query?) {
        this.typeCB.value = query?.type
        this.nameTF.text = query?.name ?: ""
        this.sqlTA.text = query?.sql ?: ""
        this.dbMessiahSqlTA.text = query?.dbMessiahSql ?: ""
    }

    private fun delete() {
        val name = this.nameTF.text
        this.queryRepo.delete(name)
    }

    private fun save() {
        val query = Query(
            name = this.nameTF.text,
            type = this.typeCB.value,
            sql = this.sqlTA.text,
            dbMessiahSql = this.dbMessiahSqlTA.text
        )
        this.queryRepo.save(query)
    }

    private fun execute() {
        this.lastResults = this.clientService.execute(sql = this.sqlTA.text)
        this.resultTA.text = this.lastResults.joinToString("\n") { it.data }
        this.updateDTO()
    }

}
