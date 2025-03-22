package com.ki.events

import java.lang.annotation.Inherited


@MustBeDocumented
@Inherited
@Retention
@Target(allowedTargets = [AnnotationTarget.TYPE, AnnotationTarget.FUNCTION])
annotation class GrpcValidation
