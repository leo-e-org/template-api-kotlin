package com.it.template.api.exception.handler

import com.it.template.api.model.exception.ApiException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.StringMapMessage
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Component
@Order(-2)
class WebExceptionHandler(
    errorAttributes: ErrorAttributes,
    applicationContext: ApplicationContext,
    configurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, WebProperties.Resources(), applicationContext) {

    init {
        super.setMessageReaders(configurer.readers)
        super.setMessageWriters(configurer.writers)
    }

    companion object {
        private const val HTTP_4XX_RANGE_START = 400
        private const val HTTP_4XX_RANGE_END = 499
        private val log = LogManager.getLogger()
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), ::renderErrorResponse)
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val exception = when (val error = getError(request)) {
            is AccessDeniedException -> ApiException(HttpStatus.FORBIDDEN)
            is ResponseStatusException -> ApiException(HttpStatus.valueOf(error.statusCode.value()))
            is ApiException -> error
            is WebClientResponseException -> ApiException(error.statusText)
            else -> ApiException(HttpStatus.INTERNAL_SERVER_ERROR, error::class.simpleName ?: "Unknown Error")
        }

        return serverResponse(exception)
    }

    private fun serverResponse(exception: ApiException): Mono<ServerResponse> {
        val isClientError = exception.code?.let { it in HTTP_4XX_RANGE_START..HTTP_4XX_RANGE_END } ?: false

        val messageLog = StringMapMessage()
            .with("message", "Web Exception Handler - response error")
            .with("app.error_code", exception.code)
            .with("error.message", exception.exceptionMessage)
            .with("error.stack_trace", exception.cause)

        if (isClientError) log.warn(messageLog) else log.error(messageLog)

        return ServerResponse.status(exception.code?.let { HttpStatus.valueOf(it) } ?: HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(exception.toString()))
    }
}
