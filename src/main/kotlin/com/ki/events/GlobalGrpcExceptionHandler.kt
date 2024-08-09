package com.ki.events

import io.grpc.Status
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler


@GrpcAdvice
class GlobalGrpcExceptionHandler {
    @GrpcExceptionHandler
    fun handleGrpcValidationException(e: GrpcValidationException): Status {
        return Status.INVALID_ARGUMENT.withDescription(e.message).withCause(e)
    }

    @GrpcExceptionHandler
    fun handleException(e: Exception): Status {
        return Status.INTERNAL.withDescription(e.message).withCause(e)
    }
}

data class GrpcValidationException(override val message: String?, override val cause: Throwable?) :
    RuntimeException()
