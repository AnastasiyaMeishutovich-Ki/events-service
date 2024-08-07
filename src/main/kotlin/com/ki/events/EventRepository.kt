package com.ki.events

import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class EventRepository {
    val events = mutableListOf<Event>()
    var nextId = 1;

    fun addEvent(dto: EventDto): Event {
        val eventWithId = Event(
            sequenceId = nextId++,
            aggregateId = dto.aggregateId,
            version = dto.version,
            data = dto.data,
            source = dto.source,
            type = dto.type,
            clientCreated = dto.clientCreated,
            serverCreated = LocalDateTime.now()
        )

        events.add(eventWithId)
        return eventWithId
    }

    fun findEventsBy(types: List<String>): List<Event> {
        return events.filter { it.type in types }
    }
}