package com.ki.events

import net.devh.boot.grpc.client.inject.GrpcClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@SpringBootTest(
    properties = ["grpc.server.inProcessName=events", "grpc.server.port=-1", "grpc.client.inProcess.address=in-process:events"
    ]
)
@DirtiesContext
class EventsApplicationTests {
    @GrpcClient("inProcess")
    var service: EventServiceGrpc.EventServiceBlockingStub? = null

    @Test
    fun shouldAddEvent() {
        val riskSavedData =
            Events.AddEventRequest.Data.newBuilder().riskSavedDataBuilder.setField1("field1").setField2("field2").build()
        val data = Events.AddEventRequest.Data.newBuilder().setRiskSavedData(riskSavedData)
        val req: Events.AddEventRequest = Events.AddEventRequest.newBuilder()
            .setAggregateId("wjekth43jkf")
            .setType(Events.AddEventRequest.EventType.RISK_SAVED)
            .setData(data)
            .build()

        val res: Events.AddEventReply? = service?.addEvent(req)

        assertNotNull(res)
        assertEquals(res.status, Events.AddEventReply.Status.CREATED)
        assertNotNull(res.timestamp)
    }
}