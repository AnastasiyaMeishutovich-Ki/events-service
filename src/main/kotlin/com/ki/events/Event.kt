package com.ki.events

import java.time.LocalDateTime

data class Event(
    var sequenceId: Int,
    val aggregateId: String,
    val version: String,
    val type: String,
    val source: String,
    val data: Map<String, Any>,
    val clientCreated: LocalDateTime,
    val serverCreated: LocalDateTime
)