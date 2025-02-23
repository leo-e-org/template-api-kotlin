package com.it.template.api.client

import com.it.template.api.model.exception.ApiException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.StringMapMessage
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono
import reactor.util.retry.Retry

abstract class HelperClient {

    companion object {
        private val log = LogManager.getLogger()
    }

    fun statusResponse(response: ClientResponse, clientName: String): Mono<Throwable> {
        val e = ApiException(HttpStatus.valueOf(response.statusCode().value()))

        if (response.statusCode().is4xxClientError)
            log.warn(
                StringMapMessage()
                    .with("message", "$clientName Client - 4xx error")
                    .with("error_code", response.statusCode().value())
                    .with("error_message", HttpStatus.valueOf(response.statusCode().value()).reasonPhrase)
                    .with("error.type", e.javaClass.name)
                    .with("error.stack_trace", e)
            )
        else
            log.error(
                StringMapMessage()
                    .with("message", "$clientName Client - 5xx error")
                    .with("error_code", response.statusCode().value())
                    .with("error_message", HttpStatus.valueOf(response.statusCode().value()).reasonPhrase)
                    .with("error.type", e.javaClass.name)
                    .with("error.stack_trace", e)
            )

        return Mono.error(e)
    }

    fun errorMap(clientName: String, methodName: String): ApiException {
        return ApiException(
            HttpStatus.GATEWAY_TIMEOUT, "$clientName Client - $methodName timed out waiting for remote client response"
        )
    }

    fun <T> errorResume(t: Throwable): Mono<T> {
        return if (t is ApiException) Mono.error(t)
        else Mono.error(ApiException(HttpStatus.INTERNAL_SERVER_ERROR, t.message))
    }

    fun retryExhaustedThrow(signal: Retry.RetrySignal, clientName: String, methodName: String): Throwable {
        log.error(
            StringMapMessage()
                .with("message", "$clientName Client - $methodName failed after max retries")
                .with("error.stack_trace", signal.failure())
        )

        throw ApiException(
            HttpStatus.INTERNAL_SERVER_ERROR, "$clientName Client - $methodName failed after max retries"
        )
    }
}
