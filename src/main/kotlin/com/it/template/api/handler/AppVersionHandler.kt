package com.it.template.api.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Component
class AppVersionHandler : MainHandler() {

    fun handleRouteVersionInfo(request: ServerRequest?): Mono<ServerResponse> {
        return ok()
            .headers(defaultHeaders())
            .bodyValue(this::class.java.`package`?.implementationVersion ?: "local")
    }
}
