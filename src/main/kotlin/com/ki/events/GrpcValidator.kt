package com.ki.events

import build.buf.protovalidate.Validator
import com.google.protobuf.Message
import jakarta.validation.ValidationException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component


@Aspect
@Component
class GrpcValidator {
    private val validator = Validator()

    @Around("@annotation(com.ki.events.GrpcValidation)")
    fun validate(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val result: Any
        try {
            val args = proceedingJoinPoint.args
            for (param in args) {
                if (param is Message) {
                    val validationResult = validator.validate(param)

                    if (validationResult.violations.isNotEmpty()) {
                        throw GrpcValidationException(validationResult.violations.toString(), null)
                    }
                }
            }
            result = proceedingJoinPoint.proceed(args)
        } catch (e: ValidationException) {
            throw GrpcValidationException(e.message, e)
        }

        return result
    }
}

//@Aspect
//@Component
//class GrpcValidator {
//    private val validator: Validator = Validator()
//
//    fun validate(message: Message) {
//        try {
//            val result: ValidationResult = validator.validate(message)
//
//            if (result.violations.isNotEmpty()) {
//                throw GrpcValidationException(result.violations.toString(), null)
//            }
//        } catch (e: ValidationException) {
//            throw GrpcValidationException(e.message, e)
//        }
//    }
//}
