package com.it.template.api.router

import com.it.template.api.handler.AppVersionHandler
import com.it.template.api.handler.function.ApiHandlerFilter
import com.it.template.api.model.AppVersion
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
open class AppVersionRouter(
    private val apiHandlerFilter: ApiHandlerFilter
) {

    @Bean
    @RouterOperation(
        operation = Operation(
            operationId = "getAppVersion",
            summary = "Get App Version",
            responses = [ApiResponse(
                responseCode = "200", content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = AppVersion::class)
                )]
            )]
        )
    )
    open fun routeVersionInfo(handler: AppVersionHandler): RouterFunction<ServerResponse> {
        return RouterFunctions.route(GET("/app-version"), handler::handleRouteVersionInfo).filter(apiHandlerFilter)
    }
}
