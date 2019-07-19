package com.dasiusp.cleber.email

import com.dasiusp.cleber.certificate.Certificate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

private const val MAILGUN_BASE_URL = "https://api.mailgun.net/v3/mailgun.dasiusp.com/messages"

@Service
class MailSender (
    private val mailgunApiKeyFetcher: MailgunApiKeyFetcher,
    private val restTemplate: RestTemplate,
    
    @Value("\${email.from}") private val from: String,
    @Value("\${email.subject}") private val subject: String,
    @Value("\${email.body}") private val body: String,
    @Value("\${email.attachment.name}") private val attachmentName: String
) {
    
    private val mailgunApiKey by lazy { mailgunApiKeyFetcher.fetchApiKey() }
    
    fun sendMail(to: String, certificate: Certificate, attachment: ByteArray) {
        restTemplate.postForObject<String>(MAILGUN_BASE_URL, HttpEntity(createBody(to, certificate, attachment), createHeaders()))
    }
    
    private fun createBody(to: String, certificate: Certificate, attachment: ByteArray): LinkedMultiValueMap<String, Any> {
        return LinkedMultiValueMap<String, Any>().apply {
            add("from", from)
            add("to", to)
            add("subject",certificate.replaceVariablesIn(subject))
            add("text", certificate.replaceVariablesIn(body))
            add("attachment", object : ByteArrayResource(attachment){
                override fun getFilename() = attachmentName
            })
        }
    }
    
    private fun createHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            setBasicAuth("api", mailgunApiKey)
            contentType = MediaType.MULTIPART_FORM_DATA
        }
    }
}
