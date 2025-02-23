package com.it.template.api.router

import com.it.template.api.handler.AppVersionHandler
import com.it.template.api.handler.function.ApiHandlerFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class AppVersionRouter(
    private val apiHandlerFilter: ApiHandlerFilter
) {

    @Bean
    fun routeVersionInfo(handler: AppVersionHandler): RouterFunction<ServerResponse> {
        return RouterFunctions.route(GET("/AppVersion"), handler::handleRouteVersionInfo).filter(apiHandlerFilter)
    }
}
