package com.dasiusp.cleber.infrastructure.repository

import com.dasiusp.cleber.type.certificate
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CertificateRepositoryTest : FunSpec() {
    
    private val firestore: Firestore = mockk()
    
    private val target = CertificateRepository(firestore, "collection")
    
    init {
        test("Should insert the document id as the certificate token") {
            val collection = mockCollection()
            
            target.insert(certificate)
            
            verify { collection.document(certificate.token.toString()) }
        }
        
        test("Should insert certificate's field in generated document") {
            val collection = mockCollection()
    
            target.insert(certificate)
    
            verify { collection.document(certificate.token.toString()).set(CertificateEntity(certificate)) }
        }
        
        test("Should get the certificate by id when it exists") {
            val collection = mockCollection()
            val token = "randomtoken1234"
            val mockedEntity = CertificateEntity("Foo", "Bar", "Bat", 42, token)
    
            every { collection.document(token).get().get().toObject(CertificateEntity::class.java) } returns mockedEntity
            
            target.get(token) shouldBe mockedEntity
        }
        
        test("Should return null when the certificate ID doesn't exist") {
            val collection = mockCollection()
            val token = "randomtoken1234"
    
            every { collection.document(token).get().get().toObject(CertificateEntity::class.java) } returns null
    
            target.get(token) shouldBe null
        }
    }
    
    private fun mockCollection(): CollectionReference {
        val collection = mockk<CollectionReference>(relaxed = true)
        every { firestore.collection("collection") } returns collection
        return collection
    }
}
