package com.it.template.api.client

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class])
@AutoConfigureWebTestClient
class SecurityApiClientTest {

    @Value("\${app.api.legacy.url:}")
    lateinit var apiLegacyUrl: String

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun shouldReturnAppVersion() {
        this.webClient = WebTestClient.bindToServer().baseUrl(apiLegacyUrl).build()
        this.webClient.post()
            .uri("/AppVersion")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Any::class.java)
            .consumeWith { response ->
                Assertions.assertNotNull(response)
                Assertions.assertNotNull(response.getResponseBody())
            }
    }
}
