package com.ki.events

import com.ki.events.domain.RiskSavedData
import org.silbertb.proto.domainconverter.annotations.ProtoConfigure;
import org.silbertb.proto.domainconverter.annotations.ProtoImport;

@ProtoImport(RiskSavedData::class)
@ProtoConfigure(converterName = "com.ki.events.Converter")
class Converter