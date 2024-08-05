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
}







