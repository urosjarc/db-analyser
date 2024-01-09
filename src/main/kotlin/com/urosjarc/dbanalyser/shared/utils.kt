package com.urosjarc.dbanalyser.shared

import javafx.concurrent.Task
import javafx.scene.control.TableColumn
import javafx.scene.control.TreeTableColumn
import kotlinx.datetime.Instant
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.sql.Timestamp

fun matchRatio(first: String, second: String): Int = FuzzySearch.weightedRatio(first, second)

fun toDateTime(timestamp: Timestamp): Instant = Instant.fromEpochMilliseconds(timestamp.time)

fun setColumnWidth(column: TableColumn<*, *>, percent: Int) {
	column.maxWidth = Integer.MAX_VALUE * percent.toDouble()
}

fun setColumnWidth(column: TreeTableColumn<*, *>, percent: Int) {
	column.maxWidth = Integer.MAX_VALUE * percent.toDouble()
}

fun startThread(sleep: Long = 0, repeat: Boolean = false, workCb: () -> Unit): Thread {
	val task: Task<Unit> = object : Task<Unit>() {
		@Throws(Exception::class)
		override fun call() {
			workCb()
			while (repeat) {
				Thread.sleep(sleep)
				workCb()
			}
		}
	}
	return Thread(task).also {
		it.isDaemon = true
		it.start()
	}
}
