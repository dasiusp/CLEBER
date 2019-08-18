package com.dasiusp.cleber.infrastructure.service.certificate

import com.dasiusp.cleber.infrastructure.repository.CertificateRepository
import org.springframework.stereotype.Service

@Service
class CertificateFinder(
    private val repository: CertificateRepository
) {

    fun find(token: String) = repository.get(token)
    
}