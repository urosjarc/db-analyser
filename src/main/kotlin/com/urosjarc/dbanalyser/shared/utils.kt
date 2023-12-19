package com.urosjarc.dbanalyser.shared

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.scene.control.TableColumn
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import kotlinx.datetime.Instant
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.sql.Timestamp

fun matchRatio(first: String, second: String): Int {
	return FuzzySearch.weightedRatio(first, second)
}

fun toDateTime(timestamp: Timestamp): Instant =
	Instant.fromEpochMilliseconds(timestamp.time)

fun startThread(sleep: Int = 0, workCb: () -> Unit): Thread {
	return Thread {
		try {
			Thread.sleep(sleep.toLong())
			workCb()
		} catch (_: InterruptedException) {

		}

	}.also { it.start() }
}

fun setColumnWidth(column: TableColumn<*, *>, percent: Int) {
	column.maxWidth = Integer.MAX_VALUE * percent.toDouble()
}

fun setColumnWidth(column: TreeTableColumn<*, *>, percent: Int) {
	column.maxWidth = Integer.MAX_VALUE * percent.toDouble()
}
