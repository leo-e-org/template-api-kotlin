package com.it.template.api.configuration.interceptor

import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class WebFilterInterceptor : WebFilter {

    companion object {
        private val log = LogManager.getLogger()
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if (getAllowedEndpoints().any { endpoint -> exchange.request.uri.path.contains(endpoint) })
            return chain.filter(exchange)

        log.info("Received HTTP {} {}", exchange.request.method, exchange.request.uri.path)

        log.debug(
            "Request origin: {} - Authorization {}",
            exchange.request.remoteAddress?.address?.hostAddress,
            exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()
        )

        val startTime = System.currentTimeMillis()
        return chain.filter(exchange).doFinally {
            val totalTime = System.currentTimeMillis() - startTime
            log.info(" HTTP {} {} finished in {}ms", exchange.request.method, exchange.request.uri.path, totalTime)
        }
    }

    protected fun getAllowedEndpoints(): List<String> {
        return listOf("actuator", "api-docs", "configuration", "v3", "webjars")
    }
}
