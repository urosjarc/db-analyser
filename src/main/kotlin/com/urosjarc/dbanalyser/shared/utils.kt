package com.urosjarc.dbanalyser.shared

import me.xdrop.fuzzywuzzy.FuzzySearch

fun matchRatio(first: String, second: String): Int {
    return FuzzySearch.weightedRatio(first, second)
}
