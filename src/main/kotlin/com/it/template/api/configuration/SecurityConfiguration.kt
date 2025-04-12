package com.it.template.api.configuration

import com.it.template.api.configuration.component.NotAuthenticatedEntryPoint
import com.it.template.api.configuration.component.SecurityAuthenticationConverter
import com.it.template.api.configuration.component.SecurityAuthenticationManager
import com.it.template.api.configuration.handler.AccessDeniedHandler
import com.it.template.api.evaluator.ApiPermissionEvaluator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableMethodSecurity
@EnableWebFluxSecurity
open class SecurityConfiguration(
    private val apiPermissionEvaluator: ApiPermissionEvaluator,
    private val securityAuthenticationConverter: SecurityAuthenticationConverter,
    private val securityAuthenticationManager: SecurityAuthenticationManager,

    @Value("\${app.config.security.cors.enabled:false}")
    private val enableCors: Boolean,
    @Value("\${app.config.security.cors.origins:}")
    private val allowedOrigins: List<String>,
    @Value("\${app.config.security.cors.methods:}")
    private val allowedMethods: List<String>,
    @Value("\${app.config.security.cors.headers:}")
    private val allowedHeaders: List<String>
) {

    @Bean
    open fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val filter = AuthenticationWebFilter(securityAuthenticationManager)
        filter.setServerAuthenticationConverter(securityAuthenticationConverter)

        return http
            .cors { if (enableCors) it.configurationSource(corsConfigurationSource()) else it.disable() }
            .logout { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .headers { it.frameOptions { it.disable() } }
            .exceptionHandling {
                it.authenticationEntryPoint(NotAuthenticatedEntryPoint()).accessDeniedHandler(AccessDeniedHandler())
            }
            .authenticationManager(securityAuthenticationManager)
            .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
            .securityMatcher(
                NegatedServerWebExchangeMatcher(
                    ServerWebExchangeMatchers.pathMatchers(*getAllowedEndpoints().toTypedArray())
                )
            )
            .authorizeExchange { it.pathMatchers(HttpMethod.OPTIONS).permitAll().anyExchange().authenticated() }
            .build()
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.applyPermitDefaultValues()
        allowedOrigins.forEach { configuration.addAllowedOrigin(it) }
        allowedMethods.forEach { configuration.addAllowedMethod(it) }
        allowedHeaders.forEach { configuration.addAllowedHeader(it) }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    open fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        expressionHandler.setPermissionEvaluator(apiPermissionEvaluator)
        return expressionHandler
    }

    protected fun getAllowedEndpoints(): List<String> {
        return listOf("/actuator/**", "/configuration/**", "/configuration/ui", "/v3/api-docs/**", "/webjars/**", "/**")
    }
}
