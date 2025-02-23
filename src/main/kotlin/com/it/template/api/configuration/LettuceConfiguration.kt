package com.it.template.api.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class LettuceConfiguration {

    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> {
        val stringSerializer = StringRedisSerializer()
        return ReactiveRedisTemplate(
            factory, RedisSerializationContext.newSerializationContext<String, String>()
                .key(stringSerializer)
                .hashKey(stringSerializer)
                .value(stringSerializer)
                .hashValue(stringSerializer)
                .build()
        )
    }
}
