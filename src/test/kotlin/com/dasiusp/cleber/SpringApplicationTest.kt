package com.dasiusp.cleber

import io.kotlintest.specs.FunSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringApplicationTest : FunSpec() {
    
    override fun listeners() = listOf(SpringListener)
    
    init {
    
        test("Context should load without any issues") {
            // This tests doesn't require any body. The annotation @SpringBootTest
            // Already covers that this class will be loaded with everything from the Spring context
            // So it's not necessary to wire anything in here.
        }
    
    }
    
}