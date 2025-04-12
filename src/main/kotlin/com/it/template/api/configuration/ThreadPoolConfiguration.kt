package com.it.template.api.configuration

import io.netty.channel.nio.NioEventLoopGroup
import org.apache.commons.lang3.concurrent.BasicThreadFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ThreadPoolConfiguration {

    companion object {
        private const val FACTORY_THREAD_NUMBER = 10
    }

    @Bean
    open fun nioEventLoopGroup(): NioEventLoopGroup {
        val factory = BasicThreadFactory.Builder()
            .namingPattern("ApiWebClientThread-%d")
            .daemon(true)
            .priority(Thread.MAX_PRIORITY)
            .build()

        return NioEventLoopGroup(FACTORY_THREAD_NUMBER, factory)
    }
}
