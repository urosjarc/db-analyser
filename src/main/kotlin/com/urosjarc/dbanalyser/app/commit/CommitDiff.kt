package com.urosjarc.dbanalyser.app.commit

import kotlinx.serialization.Serializable

@Serializable
data class CommitDiff(
    var schemasCreated: Set<String> = setOf(),
    var schemasDeleted: Set<String> = setOf(),
    var tablesCreated: Set<String> = setOf(),
    var tablesDeleted: Set<String> = setOf(),
    var columnsCreated: Set<String> = setOf(),
    var columnsDeleted: Set<String> = setOf(),
)
