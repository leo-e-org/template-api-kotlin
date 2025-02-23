package com.it.template.api.model.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true, value = ["stackTrace", "suppressed"])
class ApiException : RuntimeException {

    var code: Int? = null
    var exceptionMessage: String?

    constructor(httpStatus: HttpStatus) {
        this.code = httpStatus.value()
        this.exceptionMessage = httpStatus.reasonPhrase
    }

    constructor(httpStatus: HttpStatus, exceptionMessage: String?) {
        this.code = httpStatus.value()
        this.exceptionMessage = exceptionMessage
    }

    constructor(exceptionMessage: String) {
        this.exceptionMessage = exceptionMessage
    }

    constructor(message: String, exceptionMessage: String) : super(message) {
        this.exceptionMessage = exceptionMessage
    }

    constructor(message: String, cause: Throwable, exceptionMessage: String) : super(message, cause) {
        this.exceptionMessage = exceptionMessage
    }

    constructor(cause: Throwable, exceptionMessage: String) : super(cause) {
        this.exceptionMessage = exceptionMessage
    }

    constructor(
        message: String,
        cause: Throwable,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
        exceptionMessage: String
    ) : super(message, cause, enableSuppression, writableStackTrace) {
        this.exceptionMessage = exceptionMessage
    }

    override fun toString(): String = ObjectMapper().writeValueAsString(this)
}
