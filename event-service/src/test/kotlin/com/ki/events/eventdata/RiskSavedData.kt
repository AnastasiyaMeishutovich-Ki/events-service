package com.ki.events.eventdata

import org.silbertb.proto.domainconverter.annotations.ProtoClass;
import org.silbertb.proto.domainconverter.annotations.ProtoConstructor;
import org.silbertb.proto.domainconverter.annotations.ProtoField;

@ProtoClass(protoClass = RiskSavedData::class)
class RiskSavedData(val field1: String, val field2: Int)::Data
