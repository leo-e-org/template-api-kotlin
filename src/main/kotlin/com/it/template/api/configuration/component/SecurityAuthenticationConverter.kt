package com.it.template.api.configuration.component

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.StringMapMessage
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityAuthenticationConverter : ServerAuthenticationConverter {

    companion object {
        private val log = LogManager.getLogger()
    }

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (token.isNullOrBlank() || !token.startsWith("Bearer ")) {
            log.warn(
                StringMapMessage()
                    .with("message", "No Bearer Token found in request headers")
                    .with("app.request_headers", exchange.request.headers)
            )

            return Mono.empty()
        }

        return Mono.just(UsernamePasswordAuthenticationToken(token, token))
    }
}
