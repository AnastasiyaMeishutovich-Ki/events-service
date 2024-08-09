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
        val riskSavedData = Data.newBuilder().riskSavedDataBuilder.setField1("field1").setField2(5).build()
        val data = Data.newBuilder().setRiskSavedData(riskSavedData)
        val req: AddEventRequest = AddEventRequest.newBuilder()
            .setAggregateId("wjekth43jkf")
            .setType(EventType.RISK_SAVED)
            .setData(data)
            .build()

        val res: AddEventReply? = service?.addEvent(req)

        assertNotNull(res)
        assertEquals(res.status, AddEventReply.Status.CREATED)
        assertNotNull(res.timestamp)
    }


    @Test
    fun shouldGetEvents() {
        val req: GetEventsRequest = GetEventsRequest.newBuilder()
            .setFilters(EventFilter.newBuilder().addTypeValue(EventType.RISK_SAVED.ordinal))
            .setMax(Int32Value.of(5))
            .build()

        val res: GetEventsReply? = service?.getEvents(req)

        assertNotNull(res)
        assertEquals(1, res.eventsList.size)
        val event: Event = res.eventsList[0]
        assertEquals(event.getType(), EventType.RISK_SAVED)
        assertEquals(event.data.riskSavedData.field1, "field1")
        assertEquals(event.data.riskSavedData.field2, 5)
    }
}