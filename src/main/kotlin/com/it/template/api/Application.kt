package com.it.template.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
open class TemplateApplication

fun main(args: Array<String>) {
    runApplication<TemplateApplication>(*args)
}
