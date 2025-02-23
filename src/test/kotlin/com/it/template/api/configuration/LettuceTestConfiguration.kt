package com.it.template.api.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.ClientOptions
import io.lettuce.core.SocketOptions
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer

import java.time.Duration

@TestConfiguration
class LettuceTestConfiguration {

    companion object {
        private const val HOST = "localhost"
        private const val PORT = 6379
        private const val PASSWORD = "pwd"
        private const val CONNECTION_TIMEOUT: Long = 5000
    }

    @Bean
    fun redisTestTemplate(): RedisTemplate<String, String> {
        val serializer = GenericJackson2JsonRedisSerializer(ObjectMapper())
        val template = RedisTemplate<String, String>()
        template.connectionFactory = createConnectionFactory()
        template.keySerializer = serializer
        template.valueSerializer = serializer
        template.hashKeySerializer = serializer
        template.hashValueSerializer = serializer
        template.afterPropertiesSet()
        return template
    }

    private fun createConnectionFactory(): RedisConnectionFactory {
        val clientConfiguration = LettuceClientConfiguration.builder()
            .clientOptions(
                ClientOptions.builder()
                    .socketOptions(
                        SocketOptions.builder().connectTimeout(Duration.ofMillis(CONNECTION_TIMEOUT))
                            .build()
                    ).build()
            ).build()

        val redisConfiguration = RedisStandaloneConfiguration(HOST, PORT)
        redisConfiguration.password = RedisPassword.of(PASSWORD)

        val factory = LettuceConnectionFactory(redisConfiguration, clientConfiguration)
        factory.afterPropertiesSet()
        return factory
    }
}
