package com.dasiusp.cleber.email

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class MailgunApiKeyFetcher(
    private val restTemplate: RestTemplate,
    @Value("\${email.mailgun.api.key.url}")private val gcloudUrl: String
) {
    
    private val objectMapper = ObjectMapper()
    
    fun fetchApiKey(): String {
        val response = fetchFromRestAPI()
        return extractValueField(response)
    }
    
    private fun fetchFromRestAPI() = restTemplate.getForObject<String>(gcloudUrl)!!
    
    private fun extractValueField(response: String) = objectMapper.readTree(response)["value"].textValue()
    
    
}
