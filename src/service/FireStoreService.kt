package com.sc.coding.service

import com.google.firebase.cloud.FirestoreClient
import com.sc.coding.model.request.ImageParameterRequest
import com.sc.coding.model.request.InsertImageRequest
import com.sc.coding.model.response.ImageResponse
import com.sc.coding.model.response.Response
import org.slf4j.LoggerFactory
import java.util.*

class FireStoreService {

    private val log = LoggerFactory.getLogger(FireStoreService::class.java)
    private val db = FirestoreClient.getFirestore()
    private val IMAGE_COLLECTION = "IMAGE_COLLECTION"

    fun insertData(obj: InsertImageRequest): Response {

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
            val future = docRef.get()
            val doc = future.get()

            if (doc.exists()) {
                val res = docRef.update(dataImage)
                log.debug("Update time: ${res.get().updateTime}")
            } else {
                val res = docRef.set(dataImage)
                log.debug("Update time: ${res.get().updateTime}")
            }

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
                val imageString = document.getString("image_$seq") ?: ""
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

    fun removeData(obj: ImageParameterRequest): Response {
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
            val future = docRef.get()
            val doc = future.get()

            if (doc.exists()) {
                val res = docRef.update(dataImage)
                log.info("Update time: ${res.get().updateTime}")
            } else {
                val res = docRef.set(dataImage)
                log.info("Update time: ${res.get().updateTime}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            response.code = 503
            response.status = "Bad Request"
            response.data = e.localizedMessage
        }
        return response
    }

    fun getListImage(email: String): Response {
        val response = Response(200, "Success", emptyArray<ImageResponse>())
        try {

            val docRef = db.collection(IMAGE_COLLECTION).document(email)
            val future = docRef.get()
            val doc = future.get()

            val tempArray = mutableListOf<ImageResponse>()
            if (doc.exists()) for (key in 1..9) {
                if (!doc.getString("image_$key").isNullOrBlank()) tempArray.add(
                    ImageResponse(key.toString(), doc.get("image_$key").toString())
                )
            }
            response.data = tempArray
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }
}