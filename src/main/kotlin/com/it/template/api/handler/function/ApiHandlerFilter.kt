package com.it.template.api.handler.function

import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Component
class ApiHandlerFilter : HandlerFilterFunction<ServerResponse, ServerResponse> {

    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        return if (request.method() == HttpMethod.OPTIONS) ServerResponse.ok().body(Mono.empty<Void>())
        else next.handle(request)
    }
}
