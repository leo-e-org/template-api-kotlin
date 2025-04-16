package com.it.template.api.handler

import com.it.template.api.model.AppVersion
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Component
class AppVersionHandler : BaseHandler() {

    fun handleRouteVersionInfo(request: ServerRequest?): Mono<ServerResponse> {
        return ok()
            .headers(defaultHeaders())
            .bodyValue(AppVersion(this::class.java.`package`?.implementationVersion ?: "local"))
    }
}
