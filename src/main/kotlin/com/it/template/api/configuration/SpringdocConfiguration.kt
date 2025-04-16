package com.it.template.api.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SpringdocConfiguration {

    @Bean
    open fun openApi(): OpenAPI {
        return OpenAPI()
            .info(getInfo())
        // .components(getComponents())
        // .addSecurityItem(getSecurityItem())
    }

    private fun getInfo(): Info {
        return Info().title("Template API").description("Template API definition").version("1.0.0")
    }

    // private fun getComponents(): Components {
    //     return Components()
    //         .addSecuritySchemes(
    //             "Bearer Token", SecurityScheme()
    //                 .type(SecurityScheme.Type.HTTP)
    //                 .scheme("bearer")
    //         )
    // }

    // private fun getSecurityItem(): SecurityRequirement {
    //     return SecurityRequirement().addList("Bearer Token")
    // }
}
