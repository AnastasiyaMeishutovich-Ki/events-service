package com.ki.events.domain

import com.ki.events.Events
import org.silbertb.proto.domainconverter.annotations.ProtoBuilder;
import org.silbertb.proto.domainconverter.annotations.ProtoClass;

@ProtoBuilder
@ProtoClass(protoClass = Events.RiskSavedData::class, blacklist = true)
data class RiskSavedData(val field1: String, val field2: Int)
