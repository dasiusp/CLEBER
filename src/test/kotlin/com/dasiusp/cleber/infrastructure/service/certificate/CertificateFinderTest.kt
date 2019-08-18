package com.dasiusp.cleber.infrastructure.service.certificate

import com.dasiusp.cleber.infrastructure.repository.CertificateEntity
import com.dasiusp.cleber.infrastructure.repository.CertificateRepository
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.mockk.every
import io.mockk.mockk

class CertificateFinderTest : FunSpec() {

    
    private val repository = mockk<CertificateRepository>()
    private val target = CertificateFinder(repository)
    
    private val certificate = CertificateEntity("name", "date", "activity", 30, "token")
    
    init {
        test("Should get certificate from the repository when it exists") {
            every { repository.get("token") } returns certificate
            target.find("token") shouldBe certificate
        }
    }
}