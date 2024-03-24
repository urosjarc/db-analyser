package com.urosjarc.dbanalyser.gui.widgets.changes

import com.urosjarc.dbanalyser.app.commit.Commit
import com.urosjarc.dbanalyser.app.commit.CommitRepo
import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.input.MouseEvent
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class ChangesUi : KoinComponent {
    @FXML
    lateinit var commitB: Button

    @FXML
    lateinit var commitMsgTA: TextArea

    @FXML
    lateinit var commitsLV: ListView<Commit>

    @FXML
    lateinit var schemasLV: ListView<String>

    @FXML
    lateinit var tablesLV: ListView<String>

    @FXML
    lateinit var columnsLV: ListView<String>
}

class Changes : ChangesUi() {
    val log = this.logger()
    val schemaRepo by this.inject<SchemaRepo>()
    val commitRepo by this.inject<CommitRepo>()

    @FXML
    fun initialize() {
        this.log.info(this.javaClass)
        this.commitRepo.onData { this.commitsLV.items.setAll(it) }
        this.commitRepo.onChose { this.chose(commit = it) }
        this.commitB.setOnAction { this.commit() }
        this.commitsLV.items.setAll(this.commitRepo.data)
        this.commitsLV.setOnMouseClicked { this.commitClicked(it) }
    }

    fun commitClicked(mouseEvent: MouseEvent) {
        val commit: Commit = this.commitsLV.selectionModel.selectedItem ?: return
        this.commitRepo.chose(commit)
    }

    fun chose(commit: Commit?) {
        val commitDiff = commit?.diff(schemas = this.schemaRepo.selected) ?: return
        this.schemasLV.items.also {
            it.setAll(commitDiff.schemasCreated.map { "+++ $it" })
            it.addAll(commitDiff.schemasDeleted.map { "--- $it" })
        }
        this.tablesLV.items.also {
            it.setAll(commitDiff.tablesCreated.map { "+++ $it" })
            it.addAll(commitDiff.tablesDeleted.map { "--- $it" })
        }
        this.columnsLV.items.also {
            it.setAll(commitDiff.columnsCreated.map { "+++ $it" })
            it.addAll(commitDiff.columnsDeleted.map { "--- $it" })
        }
    }

    private fun commit() {
        val schemas = this.schemaRepo.selected
        val commit = Commit(
            name = this.commitMsgTA.text,
            schemas = schemas.map { it.toString() }.toSet(),
            tables = schemas.flatMap { it.tables.map { it.toString() } }.toSet(),
            columns = schemas.flatMap { it.tables.flatMap { it.columns.map { it.toString() } } }.toSet()
        )
        this.commitRepo.save(commit)
    }


}
