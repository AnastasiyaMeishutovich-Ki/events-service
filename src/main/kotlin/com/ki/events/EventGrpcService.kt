package com.ki.events

import com.ki.events.EventsProto.AddEventReply
import com.ki.events.EventsProto.AddEventRequest
import com.ki.events.EventsProto.Event
import com.ki.events.EventsProto.GetEventsReply
import com.ki.events.EventsProto.GetEventsRequest
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class EventGrpcService(private val repository: EventRepository) :
    EventServiceGrpc.EventServiceImplBase() {

    @GrpcValidation
    override fun addEvent(request: AddEventRequest, responseObserver: StreamObserver<AddEventReply>) {
        val event = repository.addEvent(toEventDto(request))

        val timestamp = toTimestamp(event.serverCreated)
        val reply = AddEventReply.newBuilder().setStatus(AddEventReply.Status.CREATED).setTimestamp(timestamp).build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }

    @GrpcValidation
    override fun getEvents(request: GetEventsRequest, responseObserver: StreamObserver<GetEventsReply>) {
        val types = request.filters.typeList.map { it.name }
        val events = repository.findEventsBy(types)

        val protoEvents: List<Event> = events.map { toEventProto(it).invoke() }

        val reply = GetEventsReply.newBuilder().addAllEvents(protoEvents).build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }

    override fun getEventsStreaming(request: GetEventsRequest, responseObserver: StreamObserver<Event>) {
        val types = request.filters.typeList.map { it.name }
        val events = repository.findEventsBy(types)

        val protoEvents: List<Event> = events.map { toEventProto(it).invoke() }

        protoEvents.iterator().forEachRemaining { responseObserver.onNext(it) }
        responseObserver.onCompleted()
    }
}
