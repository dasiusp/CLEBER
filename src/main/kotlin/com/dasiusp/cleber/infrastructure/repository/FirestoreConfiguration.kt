package com.dasiusp.cleber.infrastructure.repository

import com.google.cloud.firestore.FirestoreOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirestoreConfiguration {

    @Bean fun firestore() = FirestoreOptions.getDefaultInstance().service
}
