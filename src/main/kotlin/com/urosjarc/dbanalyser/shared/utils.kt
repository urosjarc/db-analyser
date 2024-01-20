package com.urosjarc.dbanalyser.shared

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
