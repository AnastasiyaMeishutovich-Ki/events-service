package com.ki.events.eventdata

import org.silbertb.proto.domainconverter.annotations.OneofBase;
import org.silbertb.proto.domainconverter.annotations.OneofField;
import org.silbertb.proto.domainconverter.annotations.ProtoClass;

@OneofBase(oneofName = "data", oneOfFields = {
    @OneofField(protoField = "riskSavedData", domainClass = RiskSavedData.class),
        @OneofField(protoField = "quoteGeneratedData", domainClass = QuoteGeneratedData.class)
})
@ProtoClass(protoClass = Data::class)
interface Data
