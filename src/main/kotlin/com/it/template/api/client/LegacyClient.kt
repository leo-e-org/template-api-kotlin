package com.it.template.api.client

import io.netty.handler.timeout.ReadTimeoutException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.scheduler.Schedulers

@Component
class LegacyClient(private val apiLegacyWebClient: WebClient) : HelperClient() {

    fun getAppVersion(): Any? {
        return apiLegacyWebClient
            .get()
            .uri("/AppVersion")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { response -> statusResponse(response, "API Legacy") }
            .toEntity(object : ParameterizedTypeReference<Any>() {})
            .mapNotNull(ResponseEntity<Any>::getBody)
            .onErrorMap(ReadTimeoutException::class.java) { errorMap("API Legacy", "getAppVersion") }
            .onErrorResume(::errorResume)
            .subscribeOn(Schedulers.boundedElastic())
            .share()
            .block()
    }
}
