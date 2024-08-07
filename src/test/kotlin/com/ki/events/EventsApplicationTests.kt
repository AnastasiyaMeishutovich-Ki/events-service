package com.ki.events

import com.ki.events.EventsProto.Event
import com.ki.events.EventsProto.EventType
import com.ki.events.EventsProto.GetEventsRequest
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
        val riskSavedData = EventsProto.Data.newBuilder().riskSavedDataBuilder.setField1("field1").setField2(5).build()
        val data = EventsProto.Data.newBuilder().setRiskSavedData(riskSavedData)
        val req: EventsProto.AddEventRequest = EventsProto.AddEventRequest.newBuilder()
            .setAggregateId("wjekth43jkf")
            .setType(EventType.RISK_SAVED)
            .setData(data)
            .build()

        val res: EventsProto.AddEventReply? = service?.addEvent(req)

        assertNotNull(res)
        assertEquals(res.status, EventsProto.AddEventReply.Status.CREATED)
        assertNotNull(res.timestamp)
    }

    /**
     * Saved in db json blob:
     *
     * {
     *   "risk_saved_data": {
     *     "field_1": "field1",
     *     "field_2": 5
     *   }
     * }
     * */

    @Test
    fun shouldGetEvents() {
        val riskSavedData = EventsProto.Data.newBuilder().riskSavedDataBuilder.setField1("field1").setField2(5).build()
        val data = EventsProto.Data.newBuilder().setRiskSavedData(riskSavedData)
        val req1: EventsProto.AddEventRequest = EventsProto.AddEventRequest.newBuilder()
            .setAggregateId("abcd1234")
            .setType(EventType.RISK_SAVED)
            .setData(data)
            .build()
        service?.addEvent(req1)

        val req: GetEventsRequest = GetEventsRequest.newBuilder()
            .setFilters(EventsProto.EventFilter.newBuilder().addTypeValue(EventType.RISK_SAVED.ordinal))
            .setMax(5)
            .build()

        val res: EventsProto.GetEventsReply? = service?.getEvents(req)

        assertNotNull(res)
        assertEquals(1, res.eventsList.size)
        val event: Event = res.eventsList[0]
        assertEquals(event.getType(), EventType.RISK_SAVED)
        assertEquals(event.data.riskSavedData.field1, "field1")
        assertEquals(event.data.riskSavedData.field2, 5)
    }
}


/**
 * proto Event:
 *
 * client_created {
 *   seconds: 1723043821
 * }
 * server_created {
 *   seconds: 1723043821
 * }
 * sequence: 1
 * data {
 *   risk_saved_data {
 *     field_1: "field1"
 *     field_2: 5
 *   }
 * }
 * */