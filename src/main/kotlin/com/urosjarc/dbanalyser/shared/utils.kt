package com.urosjarc.dbanalyser.shared

import kotlinx.datetime.Instant
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.sql.Timestamp

fun matchRatio(first: String, second: String): Int {
    return FuzzySearch.weightedRatio(first, second)
}

fun toDateTime(timestamp: Timestamp): Instant =
        Instant.fromEpochMilliseconds(timestamp.time)
