package com.it.template.api.service.cache

import com.it.template.api.repository.cache.RedisRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RedisService(
    private val redisRepository: RedisRepository
) {

    fun getValue(key: String): Mono<String> {
        return redisRepository.getValue(key)
    }

    fun setValue(key: String, value: String): Mono<Boolean> {
        return redisRepository.setValue(key, value)
    }
}
