package com.it.template.api.configuration

import com.it.template.api.model.exception.ApiException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.StringMapMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Primary
@Configuration
class WebClientConfiguration(
    @Value("\${app.api.legacy.url:}")
    private var apiLegacyUrl: String,

    @Value("\${app.config.webclient.max-memory-size:20}")
    private var maxMemorySize: Int
) {

    companion object {
        private val log = LogManager.getLogger()
        private const val MEMORY_SIZE_MULTIPLIER = 1024
    }

    @Bean
    fun apiLegacyWebClient(connector: ReactorClientHttpConnector): WebClient {
        return buildWebClient(apiLegacyUrl, connector)
    }

    private fun buildWebClient(baseUrl: String, connector: ReactorClientHttpConnector): WebClient {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(connector)
            .exchangeStrategies(
                ExchangeStrategies
                    .builder()
                    .codecs { codecs ->
                        codecs.defaultCodecs()
                            .maxInMemorySize(maxMemorySize * MEMORY_SIZE_MULTIPLIER * MEMORY_SIZE_MULTIPLIER)
                    }
                    .build()
            )
            .filter(ExchangeFilterFunction.ofResponseProcessor(::exchangeFilterResponseProcessor))
            .build()
    }

    private fun exchangeFilterResponseProcessor(response: ClientResponse): Mono<ClientResponse> {
        if (response.statusCode().isError) {
            val e = ApiException(HttpStatus.valueOf(response.statusCode().value()))

            if (response.statusCode().is4xxClientError)
                log.warn(
                    StringMapMessage()
                        .with("message", "Response Processor Filter - 4xx error")
                        .with("error_code", response.statusCode().value())
                        .with("error_message", HttpStatus.valueOf(response.statusCode().value()).reasonPhrase)
                        .with("error.stack_trace", e)
                )
            else if (response.statusCode().is5xxServerError)
                log.error(
                    StringMapMessage()
                        .with("message", "Response Processor Filter - 5xx error")
                        .with("error_code", response.statusCode().value())
                        .with("error_message", HttpStatus.valueOf(response.statusCode().value()).reasonPhrase)
                        .with("error.stack_trace", e)
                )

            return Mono.error(e)
        }

        return Mono.just(response)
    }
}
