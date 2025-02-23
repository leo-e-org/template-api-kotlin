package com.it.template.api.configuration.handler

import com.it.template.api.model.exception.ApiException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

class AccessDeniedHandler : ServerAccessDeniedHandler {

    override fun handle(exchange: ServerWebExchange, denied: AccessDeniedException): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.statusCode = HttpStatus.FORBIDDEN
        response.headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val error = ApiException(HttpStatus.FORBIDDEN).toString()
        val errorBytes = error.toByteArray(StandardCharsets.UTF_8)
        val buffer = response.bufferFactory().wrap(errorBytes)

        return response.writeWith(Mono.just(buffer))
    }
}
