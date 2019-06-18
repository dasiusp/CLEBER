package com.dasiusp.cleber.controller

import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class HelloWorldControllerTest : FunSpec() {

    private val helloController = HelloWorldController()
    
    init {
        test("Hello World should return the correct String") {
            helloController.helloWorld() shouldBe "Hello, DASI!"
        }
    }

}