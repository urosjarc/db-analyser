package com.urosjarc.dbanalyser.gui.widgets.commits

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
    lateinit var deleteB: Button

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
        this.commitRepo.onData { this.update(it) }
        this.commitRepo.onChose { this.chose(commit = it) }
        this.commitB.setOnAction { this.commit() }
        this.commitsLV.setOnMouseClicked { this.commitClicked(it) }
        this.deleteB.setOnAction { this.commitDelete() }
        this.update(this.commitRepo.data)
    }

    private fun update(it: List<Commit>) {
        this.commitsLV.items.setAll(it)
        this.commitsLV.items.sortByDescending { it.created }
    }

    private fun commitDelete() {
        val commit: Commit = this.commitsLV.selectionModel.selectedItem ?: return
        this.commitRepo.delete(commit)
    }

    fun commitClicked(mouseEvent: MouseEvent) {
        val commit: Commit = this.commitsLV.selectionModel.selectedItem ?: return
        this.commitRepo.chose(commit)
    }

    fun chose(commit: Commit?) {
        if(commit == null) return
        val prevCommit = this.commitRepo.data.filter { it.created < commit.created }.maxByOrNull { it.created } ?: return
        println("${commit.created} Comparing with ${prevCommit.created}")
        val commitDiff = commit.diff(commit = prevCommit)
        this.schemasLV.items.also {
            it.setAll(commitDiff.schemasCreated.map { "$it \t+++" })
            it.addAll(commitDiff.schemasDeleted.map { "$it \t---" })
        }
        this.schemasLV.items.sort()
        this.tablesLV.items.also {
            it.setAll(commitDiff.tablesCreated.map { "$it \t+++" })
            it.addAll(commitDiff.tablesDeleted.map { "$it \t---" })
        }
        this.tablesLV.items.sort()
        this.columnsLV.items.also {
            it.setAll(commitDiff.columnsCreated.map { "$it \t+++" })
            it.addAll(commitDiff.columnsDeleted.map { "$it \t---" })
        }
        this.columnsLV.items.sort()
    }

    private fun commit() {
        val schemas = this.schemaRepo.selected
        val commit = Commit(name = this.commitMsgTA.text, schemas = schemas)
        this.commitRepo.save(commit)
    }


}
