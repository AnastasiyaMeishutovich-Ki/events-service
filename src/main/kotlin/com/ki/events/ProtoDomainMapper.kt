package com.ki.events

import com.google.protobuf.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import com.google.protobuf.util.JsonFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


fun toEventProto(event: Event): () -> EventsProto.Event = {
    val builder = EventsProto.Data.newBuilder()
    jacksonObjectMapper().writeValueAsString(event.data)
    JsonFormat.parser().merge(jacksonObjectMapper().writeValueAsString(event.data), builder)
    val data = builder.build()

    EventsProto.Event.newBuilder()
        .setSequence(event.sequenceId!!)
        .setSource(event.source)
        .setType(EventsProto.EventType.valueOf(event.type))
        .setClientCreated(toTimestamp(event.clientCreated))
        .setServerCreated(toTimestamp(event.serverCreated))
        .setData(data)
        .build()
}

fun toEventDto(request: EventsProto.AddEventRequest): EventDto {
    val dataJson: String = JsonFormat.printer().preservingProtoFieldNames().print(request.data)
    val dataMap: Map<String, Any> = jacksonObjectMapper().readValue(dataJson)

    return EventDto(
        aggregateId = request.aggregateId,
        version = request.version,
        data = dataMap,
        source = request.source,
        type = request.type.name,
        clientCreated = LocalDateTime.now()
    )
}


fun toTimestamp(time: LocalDateTime): Timestamp =
    Timestamp.newBuilder()
        .setSeconds(time.toEpochSecond(ZoneOffset.UTC))
        .build()


