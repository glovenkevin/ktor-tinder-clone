package com.sc.coding.service

import com.google.firebase.cloud.FirestoreClient
import com.sc.coding.model.request.UserAccountUpdate
import com.sc.coding.model.response.Response
import org.slf4j.LoggerFactory

class UserAccountService {

    private val log = LoggerFactory.getLogger(UserAccountService::class.java)
    private val db = FirestoreClient.getFirestore()
    private val USER_INFORMATION = "USER_INFORMATION"

    fun insertOrUpdateInformation(obj: UserAccountUpdate): Response {

        val mapUserInfo = mapOf(
            "firstName" to obj.firstName,
            "userName" to obj.userName,
            "about" to obj.about,
            "passion" to obj.passion,
            "jobTitle" to obj.jobTitle,
            "company" to obj.company,
            "school" to obj.school,
            "city" to obj.city,
            "gender" to obj.gender,
            "birthDay" to obj.birthDay
        )
        val checkedMapUserInfo = mutableMapOf<String, String>()
        val keys = mapUserInfo.keys
        for (k in keys) {
            if (!mapUserInfo[k].isNullOrBlank()) {
                checkedMapUserInfo.put(k, mapUserInfo[k].toString())
            }
        }

        log.debug("User Account Data: $obj")

        val response = Response(200, "Success")
        try {
            val docRef = db.collection(USER_INFORMATION).document(obj.email)
            val future = docRef.get()
            val doc = future.get()

            if (doc.exists()) {
                val res = docRef.update(checkedMapUserInfo as Map<String, Any>)
                log.debug("Update time: ${res.get().updateTime}")
            } else {
                val res = docRef.set(checkedMapUserInfo as Map<String, Any>)
                log.debug("Update time: ${res.get().updateTime}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            response.code = 500
            response.status = "Error"
            response.data = e.localizedMessage
        }

        return response
    }

}