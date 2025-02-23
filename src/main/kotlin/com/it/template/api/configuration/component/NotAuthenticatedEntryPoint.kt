package com.it.template.api.configuration.component

import com.it.template.api.model.exception.ApiException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

class NotAuthenticatedEntryPoint : ServerAuthenticationEntryPoint {

    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        response.headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val error = ApiException(HttpStatus.UNAUTHORIZED).toString()
        val errorBytes = error.toByteArray(StandardCharsets.UTF_8)
        val buffer = response.bufferFactory().wrap(errorBytes)

        return response.writeWith(Mono.just(buffer))
    }
}
