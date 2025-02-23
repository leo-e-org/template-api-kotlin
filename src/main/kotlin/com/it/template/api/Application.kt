package com.it.template.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan

@EnableCaching
@ComponentScan(basePackages = ["com.it"])
@SpringBootApplication
class TemplateApplication

fun main(args: Array<String>) {
    runApplication<TemplateApplication>(*args)
}
