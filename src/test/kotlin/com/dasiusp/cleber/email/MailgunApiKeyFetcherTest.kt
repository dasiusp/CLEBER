package com.dasiusp.cleber.email

import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

class MailgunApiKeyFetcherTest : FunSpec() {


    private val restTemplate: RestTemplate = mockk()
    
    private val target = MailgunApiKeyFetcher(restTemplate, expectedGCloudUrl)
    
    init {
        test("Should make a plain get request") {
            mockRestTemplate()
            
            target.fetchApiKey() shouldBe mockedMailgunApiKey
        
        }
    }
    
    private fun mockRestTemplate() {
        every { restTemplate.getForObject<String>(expectedGCloudUrl) } returns apiResponseString
    }
}

private val mockedMailgunApiKey = "foobar"

// Api response and url format extracted from the official documentation
// https://cloud.google.com/deployment-manager/runtime-configurator/set-and-get-variables
private val expectedGCloudUrl = "https://runtimeconfig.googleapis.com/v1beta1/projects/cleber-244103/configs/APIs/variables/mailgun"
private val apiResponseString = """
        {
            "name": "projects/cleber-244103/configs/APIs/variables/mailgun",
            "value": "$mockedMailgunApiKey",
            "updateTime": "2016-04-11T21:49:00.773366134Z"
        }
    """.trimIndent()
