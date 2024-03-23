package com.urosjarc.dbanalyser.shared

import com.urosjarc.dbanalyser.app.schema.Schema
import com.urosjarc.dbanalyser.app.table.Table
import javafx.application.Platform
import javafx.concurrent.Task
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TreeTableColumn
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import kotlinx.datetime.Instant
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.apache.logging.log4j.kotlin.logger
import java.sql.Timestamp

fun matchRatio(first: String, second: String): Int = FuzzySearch.weightedRatio(first, second)

fun toDateTime(timestamp: Timestamp): Instant = Instant.fromEpochMilliseconds(timestamp.time)

fun setColumnWidth(column: TableColumn<*, *>, percent: Int) {
    column.maxWidth = Integer.MAX_VALUE * percent.toDouble()
}

fun setColumnWidth(column: TreeTableColumn<*, *>, percent: Int) {
    column.maxWidth = Integer.MAX_VALUE * percent.toDouble()
}

fun setCopy(label: Label) {
    val clipboard: Clipboard = Clipboard.getSystemClipboard()
    val content = ClipboardContent()

    var oldText = ""

    label.setOnMousePressed {
        content.putString(label.text)
        clipboard.setContent(content)
        oldText = label.text
        label.text = "(Copied)"
    }
    label.setOnMouseReleased {
        startThread(sleep = 500) {
            Platform.runLater { label.text = oldText }
        }
    }
}

fun startThread(sleep: Long = 0, interval: Long = 0, repeat: Boolean = false, workCb: () -> Unit): Thread {

    val task: Task<Unit> = object : Task<Unit>() {
        val log = logger(Thread.currentThread().name)

        @Throws(Exception::class)
        override fun call() {
            Thread.sleep(sleep)
            this.work()
            while (repeat) {
                Thread.sleep(interval)
                this.work()
            }
        }

        fun work() = try {
            workCb()
        } catch (e: Throwable) {
            this.log.fatal(e)
        }
    }
    return Thread(task).also {
        it.isDaemon = true
        it.start()
    }
}

fun dbdiagram_io(tables: List<Table>): String {
    val lines = mutableListOf<String>()
    val refs = mutableListOf<String>()
    tables.forEach { table ->
        val schema = table.schema!!.name.split(".").last()
        lines.add("Table $schema.${table.name} {")
        table.columns.forEach { column ->
            val primaryKey = if (column.primaryKey) " [primary key]" else ""
            lines.add("\t${column.name} ${column.baseType}$primaryKey")
            if (column.isForeignKey) {
                val fromSchema = column.table.schema!!.name.split(".").last()
                val fk = column.foreignKey!!
                val toSchema = fk.to.table.schema!!.name.split(".").last()
                refs.add("Ref: $fromSchema.${column.table.name}.${column.name} > $toSchema.${fk.to.table.name}.${fk.to.name}")
            }
        }
        lines.add("}\n")
    }

    return (lines + refs).joinToString("\n")
}

fun plantUML(schemas: List<Schema>): String {
    val refs = mutableListOf<String>()
    val lines = mutableListOf(
        "@startuml", "skinparam backgroundColor darkgray", "skinparam ClassBackgroundColor lightgray"
    )
    schemas.forEach { s ->
        lines.add("package ${s.name} <<Folder>> {")
        s.tables.forEach { t ->
            lines.add("\t class ${t.name} {")
            if (t.primaryKey != null) lines.add("\t\t ${t.primaryKey!!.name}: ${t.primaryKey!!.type}")
            t.foreignKeys.forEach {
                refs.add("${it.from.table.name} -down-> ${it.to.table.name}: ${it.from.name}")
                lines.add("\t\t ${it.from.name}: ${it.from.type}")
            }
            t.columns.forEach { lines.add("\t\t ${it.name}: ${it.type}") }
            lines.add("\t }")
        }
        lines.add("}")
    }

    lines.add("")
    lines.addAll(refs)
    lines.add("")
    lines.add("@enduml")

    return lines.joinToString("\n")
}
