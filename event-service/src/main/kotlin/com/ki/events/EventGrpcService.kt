package com.ki.events

import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import java.time.Instant

@GrpcService
class EventGrpcService: EventServiceGrpc.EventServiceImplBase() {
    override fun addEvent(request: AddEventRequest?, responseObserver: StreamObserver<AddEventReply>?) {
        val currentTime = Instant.now()
        val timestamp = Timestamp.newBuilder()
            .setSeconds(currentTime.epochSecond)
            .setNanos(currentTime.nano)
            .build()
        val reply = AddEventReply.newBuilder().setStatus(AddEventReply.Status.CREATED).setTimestamp(timestamp).build()
        if (responseObserver != null) {
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }

    override fun getEvents(request: GetEventsRequest?, responseObserver: StreamObserver<GetEventsReply>?) {
        var event:Event
        request?.filters?.typeList.let {
        val riskSavedData: RiskSavedData = Event.newBuilder().data.newBuilderForType().riskSavedData.newBuilderForType().setField1("field1").setField2(5).build()
        val data = Data.newBuilder().setRiskSavedData(riskSavedData)
            event = Event.newBuilder().setData(data).build()
        }

        val reply = GetEventsReply.newBuilder().addEvents(event).build()
            if (responseObserver != null) {
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }
}







