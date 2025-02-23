package com.it.template.api.repository.cache

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class RedisRepository(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,

    @Value("\${app.config.cache.custom-key-ttl:3600}")
    private var keyTimeout: Long
) {

    fun getValue(key: String): Mono<String> {
        return reactiveRedisTemplate.opsForValue().get(key).defaultIfEmpty("")
    }

    fun setValue(key: String, value: String): Mono<Boolean> {
        return reactiveRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(keyTimeout))
    }
}
