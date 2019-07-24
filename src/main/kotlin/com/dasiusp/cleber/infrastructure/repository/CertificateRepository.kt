package com.dasiusp.cleber.infrastructure.repository

import com.dasiusp.cleber.type.Certificate
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.annotation.PropertyName
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.time.format.DateTimeFormatter

@Repository
class CertificateRepository(
    private val firestore: Firestore,
    @Value("\${firestore.certificate.collection}") private val collectionName: String
) {
    
    private val collection: CollectionReference
    get() = firestore.collection(collectionName)
    
    fun insert(certificate: Certificate) {
        collection.document("${certificate.token}").set(
            CertificateEntity(
                certificate
            )
        ).get()
    }
}

data class CertificateEntity(
    @set:PropertyName("person_name")
    @get:PropertyName("person_name")
    var personName: String,
    
    @set:PropertyName("activity_date")
    @get:PropertyName("activity_date")
    var isoFormattedDate: String,
    
    @set:PropertyName("activity_name")
    @get:PropertyName("activity_name")
    var activityName: String,
    
    @set:PropertyName("activity_duration_hours")
    @get:PropertyName("activity_duration_hours")
    var durationInHours: Int,
    
    @set:PropertyName("token")
    @get:PropertyName("token")
    var token: String
) {
    constructor(certificate: Certificate) : this(
        certificate.personName,
        certificate.activityDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
        certificate.activityName,
        certificate.durationInHours,
        certificate.token.toString()
    )
}