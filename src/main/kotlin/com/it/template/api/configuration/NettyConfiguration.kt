package com.it.template.api.configuration

import io.netty.channel.ChannelOption
import io.netty.channel.epoll.EpollChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ReactorResourceFactory
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.resources.ConnectionProvider

import java.time.Duration

@Configuration
open class NettyConfiguration(
    @Value("\${app.api.request-timeout:30}")
    private var responseTimeout: Long
) {

    companion object {
        private const val NETTY_CONNECT_TIMEOUT_MILLIS = 10000
        private const val NETTY_TCP_KEEPCNT = 10
        private const val NETTY_TCP_KEEPIDLE = 90
        private const val NETTY_TCP_KEEPINTVL = 60
        private const val NETTY_SO_BACKLOG = 2048

        private const val PROVIDER_MAX_CONNECTIONS = 2000
        private const val PROVIDER_ACQUIRE_TIMEOUT: Long = 60

        private const val REACTOR_CONNECT_TIMEOUT_MILLIS = 10000
    }

    @Bean
    open fun nettyReactiveWebServerFactory(): NettyReactiveWebServerFactory {
        return NettyReactiveWebServerFactory().apply {
            addServerCustomizers({ server ->
                server.childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, NETTY_CONNECT_TIMEOUT_MILLIS)
                    .childOption(EpollChannelOption.TCP_KEEPCNT, NETTY_TCP_KEEPCNT)
                    .childOption(EpollChannelOption.TCP_KEEPIDLE, NETTY_TCP_KEEPIDLE)
                    .childOption(EpollChannelOption.TCP_KEEPINTVL, NETTY_TCP_KEEPINTVL)
                    .option(ChannelOption.SO_BACKLOG, NETTY_SO_BACKLOG)
            })
        }
    }

    @Bean
    open fun reactorResourceFactory(nioEventLoopGroup: NioEventLoopGroup): ReactorResourceFactory {
        return ReactorResourceFactory().apply {
            connectionProvider = ConnectionProvider.builder("fixed")
                .maxConnections(PROVIDER_MAX_CONNECTIONS)
                .pendingAcquireTimeout(Duration.ofSeconds(PROVIDER_ACQUIRE_TIMEOUT))
                .build()
            setLoopResources { nioEventLoopGroup }
            isUseGlobalResources = false
        }
    }

    @Bean
    open fun reactorClientHttpConnector(reactorResourceFactory: ReactorResourceFactory): ReactorClientHttpConnector {
        return ReactorClientHttpConnector(reactorResourceFactory) { m ->
            m.followRedirect(true)
                .responseTimeout(Duration.ofSeconds(responseTimeout))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, REACTOR_CONNECT_TIMEOUT_MILLIS)
        }
    }
}
