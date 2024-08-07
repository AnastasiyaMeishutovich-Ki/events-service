package com.ki.events

import java.time.LocalDateTime

data class EventDto(
    val aggregateId: String,
    val version: String,
    val type: String,
    val source: String,
    val data: Map<String, Any>,
    val clientCreated: LocalDateTime
)