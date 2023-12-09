package com.urosjarc.dbanalyser.app.logs

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
data class Log(
    val type: Type,
    val data: String,
    val createdAt: Instant = Clock.System.now(),
) {
    enum class Type { DEBUG, INFO, WARN, ERROR, FATAL }

    val time get() = this.createdAt.toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).time

}
