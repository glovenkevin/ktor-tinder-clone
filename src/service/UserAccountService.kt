package com.sc.coding.service

import com.google.cloud.firestore.Query
import com.google.firebase.cloud.FirestoreClient
import com.sc.coding.model.request.RandomUserParam
import com.sc.coding.model.request.UserAccountUpdate
import com.sc.coding.model.response.Response
import org.slf4j.LoggerFactory
import java.util.*

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
            "birthDay" to obj.birthDay,
            "token" to obj.token
        )
        val checkedMapUserInfo = mutableMapOf<String, Any>()
        val keys = mapUserInfo.keys
        for (k in keys) {
            if (!mapUserInfo[k].isNullOrBlank()) {
                checkedMapUserInfo.put(k, mapUserInfo[k].toString())
            }
        }

        if (obj.birthDay.isNotBlank()) {
            val year = obj.birthDay.split("-")[0].toInt()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val age = currentYear - year
            checkedMapUserInfo.put("age", age)
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

    /*
        rule:
        1. show by age and gender
        2. don't show again for the showed profile

        plan:
        1. get
     */
    fun getRandomUser(param: RandomUserParam): Response {
        val response = Response(200, "Success")
        try {

            if (param.age == 0 || param.gender.isBlank() || param.userName.isBlank()) {
                response.code = 503
                response.status = "Bad Request"
                response.data = "Data tidak lengkap"
                return response
            }

            val collection = db.collection(USER_INFORMATION)
            val query = collection.whereNotEqualTo("userName",param.userName)
                .whereEqualTo("gender", param.gender)
                .whereGreaterThanOrEqualTo("age", param.age)
                .orderBy("age", Query.Direction.ASCENDING)


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

}