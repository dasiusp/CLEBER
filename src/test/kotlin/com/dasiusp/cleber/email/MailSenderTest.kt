package com.dasiusp.cleber.email

import com.dasiusp.cleber.certificate.Certificate
import io.kotlintest.IsolationMode
import io.kotlintest.IsolationMode.InstancePerTest
import io.kotlintest.matchers.collections.shouldHaveSingleElement
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.Base64Utils
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import java.time.LocalDate

class MailSenderTest : FunSpec() {
    
    private val restTemplate: RestTemplate = mockk(relaxed = true)
    private val mailgunApiKeyFetcher = mockKeyFetcher()
    
    private val target = MailSender(mailgunApiKeyFetcher, restTemplate, "example@example.com", "%NOME_PESSOA%", "%NOME_PESSOA%", "Certificado.pdf")
    
    init {
        test("Should get key from api fetcher") {
            sendMail()
            verify(exactly = 1){ mailgunApiKeyFetcher.fetchApiKey() }
        }
        
        test("Should cache key request instead of calling it many times") {
            sendMail()
            sendMail()
            verify(exactly = 1){ mailgunApiKeyFetcher.fetchApiKey() }
        }
        
        test("Should make a post to mailgun api while authenticated") {
            sendMail()
            shouldHavePosted()
        }
        
        test("Should make the request authenticated with the api key") {
            sendMail()
            shouldHavePosted {
                it.headers[HttpHeaders.AUTHORIZATION]!! shouldHaveSingleElement "Basic $mailgunUserPassBase64"
            }
        }
        
        test("Should have content type as multi-part form data") {
            sendMail()
            shouldHavePosted {
                it.headers.contentType shouldBe MediaType.MULTIPART_FORM_DATA
            }
        }
        
        test("Should have 'from' field injected from properties") {
            sendMail()
            shouldHavePosted {
                it.shouldContainInBody("from", "example@example.com")
            }
        }
        
        test("Should have 'to' from the parameters") {
            sendMail()
            shouldHavePosted {
                it.shouldContainInBody("to", "to@example.com")
            }
        }
        
        test("Should have 'subject' and 'text' as configured in the constructor, replacing variables") {
            sendMail()
            shouldHavePosted {
                it.shouldContainInBody("subject", "pname")
                it.shouldContainInBody("text", "pname")
            }
        }
        
        test("Should have 'attachment' as the byte array passed as parameter") {
            sendMail()
            shouldHavePosted {
                it.shouldContainInBody("attachment", ByteArrayResource("attachment!".toByteArray()))
            }
        }
        
    }
    
    private fun sendMail() {
        val certificate = Certificate("pname", LocalDate.now(), "etype", "ename", 40, "token")
        target.sendMail("to@example.com", certificate, "attachment!".toByteArray())
    }
    
    private fun HttpEntity<LinkedMultiValueMap<String, Any>>.shouldContainInBody(key: String, value: Any) {
        body[key]!![0] shouldBe value
    }
    
    private fun shouldHavePosted(block: (HttpEntity<LinkedMultiValueMap<String, Any>>) -> Unit = {} ) {
        val slot = slot<HttpEntity<LinkedMultiValueMap<String,Any>>>()
        verify(exactly = 1) { restTemplate.postForObject<String>("https://api.mailgun.net/v3/mailgun.dasiusp.com/messages", capture(slot)) }
        block(slot.captured)
    }
    
    private fun mockKeyFetcher(): MailgunApiKeyFetcher {
        return mockk { every { fetchApiKey() } returns "mailgun" }
    }
    
    private val mailgunUserPassBase64 = Base64Utils.encodeToUrlSafeString("api:mailgun".toByteArray())
    
    override fun isolationMode(): IsolationMode = InstancePerTest
}
