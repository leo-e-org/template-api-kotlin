package com.it.template.api.configuration.component

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class SecurityAuthenticationManager : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return Mono.just(
            UsernamePasswordAuthenticationToken(
                authentication.principal, authentication.credentials, authentication.authorities
            )
        )
    }
}
