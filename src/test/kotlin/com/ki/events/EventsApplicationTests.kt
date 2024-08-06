package com.ki.events

import com.google.protobuf.Int32Value
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
        val riskSavedData = Events.Data.newBuilder().riskSavedDataBuilder.setField1("field1").setField2(5).build()
        val data = Events.Data.newBuilder().setRiskSavedData(riskSavedData)
        val req: Events.AddEventRequest = Events.AddEventRequest.newBuilder()
            .setAggregateId("wjekth43jkf")
            .setType(Events.EventType.RISK_SAVED)
            .setData(data)
            .build()

        val res: Events.AddEventReply? = service?.addEvent(req)

        assertNotNull(res)
        assertEquals(res.status, Events.AddEventReply.Status.CREATED)
        assertNotNull(res.timestamp)
    }


    @Test
    fun shouldGetEvents() {
        val req: Events.GetEventsRequest = Events.GetEventsRequest.newBuilder()
            .setFilters(Events.EventFilter.newBuilder().addTypeValue(Events.EventType.RISK_SAVED.ordinal))
            .setMax(Int32Value.of(5))
            .build()

        val res: Events.GetEventsReply? = service?.getEvents(req)

        assertNotNull(res)
        assertEquals(1, res.eventsList.size)
        val event: Events.Event = res.eventsList[0]
        assertEquals(event.getType(), Events.EventType.RISK_SAVED)
        assertEquals(event.data.riskSavedData.field1, "field1")
        assertEquals(event.data.riskSavedData.field2, 5)
    }
}