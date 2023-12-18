package com.urosjarc.dbanalyser.shared

import javafx.application.Platform
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
		} catch (_: InterruptedException){

		}

	}.also { it.start() }
}
