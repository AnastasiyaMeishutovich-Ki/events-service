package com.ki.events

import com.ki.events.domain.RiskSavedData
import org.junit.Assert.assertEquals
import kotlin.test.Test

class UsageDomainTest {

    @Test
    fun testToProto() {
        val domain = RiskSavedData("field1", 2)
        val proto = Events.RiskSavedData.newBuilder().setField1("field1").setField2(2).build()
        val result = ProtoDomainConverter.toProto(domain); // DOESN'T COMPILE
        assertEquals(result, proto);
    }
}