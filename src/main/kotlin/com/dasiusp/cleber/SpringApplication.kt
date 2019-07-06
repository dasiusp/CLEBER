package com.dasiusp.cleber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class SpringApplication

fun main() {
    runApplication<SpringApplication>()
}

class ServletInitializer : SpringBootServletInitializer() {
    
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(SpringApplication::class.java)
    }
    
}