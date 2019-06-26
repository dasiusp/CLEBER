package com.dasiusp.cleber.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class HelloWorldController {

    
    @GetMapping("/hello")
    fun helloWorld() = "Hello, DASI!"
    
}