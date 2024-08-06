package com.ki.events

import com.google.protobuf.Timestamp
import com.ki.events.Events.AddEventReply
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import java.time.Instant

@GrpcService
class EventGrpcService: EventServiceGrpc.EventServiceImplBase() {
    override fun addEvent(request: Events.AddEventRequest?, responseObserver: StreamObserver<AddEventReply>?) {
        val currentTime = Instant.now()
        val timestamp = Timestamp.newBuilder()
            .setSeconds(currentTime.epochSecond)
            .setNanos(currentTime.nano)
            .build()
        val reply = AddEventReply.newBuilder().setStatus(Events.AddEventReply.Status.CREATED).setTimestamp(timestamp).build()
        if (responseObserver != null) {
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }

    override fun getEvents(request: Events.GetEventsRequest?, responseObserver: StreamObserver<Events.GetEventsReply>?) {

        var event:Events.Event
        request?.filters?.typeList.let {
        val riskSavedData: Events.RiskSavedData = Events.Event.newBuilder().dataBuilder.riskSavedDataBuilder.setField1("field1").setField2("field2").build()
        val data = Events.Data.newBuilder().setRiskSavedData(riskSavedData)
            event = Events.Event.newBuilder().setData(data).build()
        }

        val reply = Events.GetEventsReply.newBuilder().addEvents(event).build()
            if (responseObserver != null) {
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }
}







