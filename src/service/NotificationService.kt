package com.sc.coding.service

import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.sc.coding.configuration.GlobalConfiguration
import com.sc.coding.model.request.NotificationRequest
import com.sc.coding.model.response.Response
import org.slf4j.LoggerFactory

class NotificationService {

    private val db = FirestoreClient.getFirestore()
    private val log = LoggerFactory.getLogger(UserAccountService::class.java)

    fun sendNotification(param: NotificationRequest): Response {
        val response = Response(200, "Success")
        if (param.emailTo.isBlank() || param.title.isBlank() || param.message.isBlank()) {
            response.code = 503
            response.status = "Bad Request"
            response.data = "Data tidak lengkap"
            return response
        }

        try {

            val docRef = db.collection(GlobalConfiguration.USER_INFORMATION).document(param.emailTo)
            val future = docRef.get()
            val doc = future.get()

            val token = doc.getString("token")
            val message = Message.builder()
                .putData("title", param.title)
                .putData("message", param.message)
                .setToken(token)
                .build()

            val stringResp = FirebaseMessaging.getInstance()
                .send(message)
            log.info("Successfully sent message: $stringResp")

        } catch (e: Exception) {
            e.printStackTrace()
            response.code = 500
            response.status = "Error"
            response.data = e.localizedMessage
        }
        return response
    }

}