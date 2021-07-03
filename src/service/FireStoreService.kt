package com.sc.coding.service

import com.google.firebase.cloud.FirestoreClient
import com.sc.coding.model.request.ImageParameter
import com.sc.coding.model.request.InsertImage
import com.sc.coding.model.response.Response
import org.slf4j.LoggerFactory
import java.util.*

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
            val res = docRef.update(dataImage)
            log.info("Update time: ${res.get().updateTime}")
        } catch (e: Exception) {
            e.printStackTrace()
            response.code = 503
            response.status = "Bad Request"
            response.data = e.localizedMessage
        }
        return response
    }

    fun getImage(email: String, seq: String): ByteArray? {
        var rtn: ByteArray? = null
        try {
            val doc = db.collection(IMAGE_COLLECTION).document(email)
            val future = doc.get()
            val document = future.get()

            if (document.exists()) {
                var imageString = document.getString("image_$seq") ?: ""
                if (imageString.isNotBlank()) {
                    rtn = Base64.getDecoder().decode(imageString)
                } else {
                    log.warn("Image not found for user $email seq $seq")
                }
            } else {
                log.warn("Image not found for user $email seq $seq")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rtn
    }

    fun removeData(obj: ImageParameter): Response {
        if (obj.email.isBlank()
            || obj.seq.isBlank()) {
            return Response(503, "Bad Request", "Data tidak lengkap")
        }

        val dataImage = mapOf(
            "image_${obj.seq}" to ""
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
            response.data = e.localizedMessage
        }
        return response
    }

}