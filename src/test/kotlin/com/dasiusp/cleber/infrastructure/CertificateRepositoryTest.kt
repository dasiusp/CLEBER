package com.dasiusp.cleber.infrastructure

import com.dasiusp.cleber.certificate.Certificate
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import io.kotlintest.specs.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

class CertificateRepositoryTest : FunSpec() {
    
    private val firestore: Firestore = mockk()
    
    private val target = CertificateRepository(firestore, "collection")
    
    private val certificate = Certificate("Person", LocalDate.of(1998, 2, 9), "EventType", "EventName", 30, "token")
    
    init {
        test("Should insert the document id as the certificate token") {
            val collection = mockCollection()
            
            target.insert(certificate)
            
            verify { collection.document(certificate.token) }
        }
        
        test("Should insert certificate's field in generated document") {
            val collection = mockCollection()
    
            target.insert(certificate)
    
            verify { collection.document("token").set(CertificateEntity(
                "Person", "1998-02-09", "EventName", 30, "token"
            )) }
        }
    }
    
    private fun mockCollection(): CollectionReference {
        val collection = mockk<CollectionReference>(relaxed = true)
        every { firestore.collection("collection") } returns collection
        return collection
    }
}

class Foo : FunSpec() {
    
    data class Data(val value: String)
    
    class Dooter {
        fun doot() {}
    }
    class Booer {
        fun boo(data: Data): Dooter {
            return Dooter()
        }
    }
    
    class Fooer {
        fun foo(data: Data): Booer {
            return Booer()
        }
    }
    
    class Target(
        val fooer: Fooer
    ) {
        fun doThings(data: Data) {
            fooer.foo(data).boo(data).doot()
        }
    }
    
    init {
        test("Test") {
            val fooer = mockk<Fooer>(relaxed = true)
            val target = Target(fooer)
            val data = Data("Data")
            
            target.doThings(data)
            
            verify { fooer.foo(data).boo(any()).doot() }
        }
    }
}