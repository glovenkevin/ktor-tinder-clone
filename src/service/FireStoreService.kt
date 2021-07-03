package com.sc.coding.service

import com.google.firebase.cloud.FirestoreClient
import com.sc.coding.model.request.InsertImage
import com.sc.coding.model.response.Response
import org.slf4j.LoggerFactory

class FireStoreService {

    private val log = LoggerFactory.getLogger(FireStoreService::class.java)
    private val db = FirestoreClient.getFirestore()
    private val IMAGE_COLLECTION = "IMAGE_COLLECTION"

    fun insertData(obj: InsertImage): Response {

        if (obj.email.isBlank()
            || obj.imageBase64.isBlank()
            || obj.seq.isBlank()) {
            return Response(503, "Bad Request", "Data tidak lengkap")
        }

        val dataImage = mapOf(
            "image_${obj.seq}" to obj.imageBase64
        )

        val response = Response(200, "Success")
        try {
            val docRef = db.collection(IMAGE_COLLECTION).document(obj.email)
            val res = docRef.set(dataImage)
            log.info("Update time: ${res.get().updateTime}")
        } catch (e: Exception) {
            e.printStackTrace()
            response.code = 503
            response.status = "Bad Request"
        }
        return response
    }

}