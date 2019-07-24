package com.dasiusp.cleber.infrastructure.service.email

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport
import com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance
import com.google.api.services.runtimeconfig.v1beta1.CloudRuntimeConfig
import com.google.auth.appengine.AppEngineCredentials
import com.google.auth.http.HttpCredentialsAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MailgunApiKeyFetcher(
    @Value("\${project.id}") private val projectId: String
) {
    
    val runtimeConfigClient by lazy {
        val credential = AppEngineCredentials.getApplicationDefault()
        CloudRuntimeConfig(newTrustedTransport(), getDefaultInstance(), HttpCredentialsAdapter(credential))
    }
    
    fun fetchApiKey(): String {
        return runtimeConfigClient.Projects().Configs().variables()
            .get("projects/$projectId/configs/APIs/variables/mailgun")
            .execute().text
    }
    
}
