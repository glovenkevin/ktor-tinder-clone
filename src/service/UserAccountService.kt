package com.sc.coding.service

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.cloud.firestore.QuerySnapshot
import com.google.firebase.cloud.FirestoreClient
import com.sc.coding.model.entity.DataUserRandomShowEntity
import com.sc.coding.model.request.RandomUserParam
import com.sc.coding.model.request.UserAccountUpdate
import com.sc.coding.model.response.RandomUserInfo
import com.sc.coding.model.response.Response
import org.slf4j.LoggerFactory
import java.util.*

class UserAccountService {

    private val log = LoggerFactory.getLogger(UserAccountService::class.java)
    private val db = FirestoreClient.getFirestore()
    private val USER_INFORMATION = "USER_INFORMATION"
    private val USER_RANDOM_SHOW = "USER_RANDOM_SHOW"

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
        1. get document random show , check if exist, if not create it
        2. if document not exist
            - get data using default condition (age and gender)
            - save the data on current showed
            - return the user data
        3. if document exist
            - check if status Y or N
            - if status == Y put it on whitelist
            - if status == N put it on blacklist
            - search user except blacklist and whitelist and default condition
            - update current showed user and return the username
        4. return the result
     */
    fun getRandomUser(param: RandomUserParam): Response {
        val response = Response(200, "Success", RandomUserInfo())
        try {

            if (param.age == 0 || param.gender.isBlank()
                || param.userName.isBlank() || param.email.isBlank()) {
                response.code = 503
                response.status = "Bad Request"
                response.data = "Data tidak lengkap"
                return response
            }

            val docRefRandomShow = db.collection(USER_RANDOM_SHOW).document(param.email)
            val futureRandomShow = docRefRandomShow.get()
            val docRandomShow = futureRandomShow.get()

            if (docRandomShow.exists()) {

                if (param.status.isBlank()) {
                    response.code = 503
                    response.status = "Bad Request"
                    response.data = "Status belum terisi"
                    return response
                }

                val dataUserRandom = docRandomShow.toObject(DataUserRandomShowEntity::class.java)
                if (null != dataUserRandom) {
                    val userNameNotIn = mutableListOf<String>()
                    userNameNotIn.addAll(dataUserRandom.blackList)
                    userNameNotIn.addAll(dataUserRandom.whiteList)
                    userNameNotIn.add(dataUserRandom.currentEmail)

                    val collection = db.collection(USER_INFORMATION)
                    val query = collection.whereEqualTo("gender", param.gender)
                        .whereGreaterThanOrEqualTo("age", param.age)
                        .orderBy("age", Query.Direction.ASCENDING)
                        .startAfter(dataUserRandom.startAfter)
                        .limit(10)

                    val querySnapshot = query.get()
                    for ((count, objDoc) in querySnapshot.get().documents.withIndex()) {
                        val email = objDoc.id
                        if (!email.equals(param.email, true)
                            && !userNameNotIn.contains(email)) {
                            response.data = this.updateRandomShowUser(objDoc)

                            if (param.status.isNotBlank()) if (param.status.equals("y", ignoreCase = true)) {
                                dataUserRandom.whiteList.add(dataUserRandom.currentEmail)
                            } else {
                                dataUserRandom.blackList.add(dataUserRandom.currentEmail)
                            }

                            val paramUpdate = mapOf(
                                "currentEmail" to email,
                                "startAfter" to dataUserRandom.startAfter + count,
                                "whiteList" to dataUserRandom.whiteList,
                                "blackList" to dataUserRandom.blackList
                            )
                            docRefRandomShow.update(paramUpdate)
                            break
                        }
                    }
                }

            } else {
                val collection = db.collection(USER_INFORMATION)
                val query = collection.whereEqualTo("gender", param.gender)
                    .whereGreaterThanOrEqualTo("age", param.age)
                    .orderBy("age", Query.Direction.ASCENDING)
                    .startAfter(0)
                    .limit(10)

                val querySnapshot = query.get()
                for ((count, objDoc) in querySnapshot.get().documents.withIndex()) {
                    val email = objDoc.id
                    if (!email.equals(param.email, true)) {
                        response.data = this.updateRandomShowUser(objDoc)

                        val paramInsert = DataUserRandomShowEntity(
                            currentEmail = email,
                            startAfter = count
                        )
                        docRefRandomShow.set(paramInsert)
                        break
                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    private fun updateRandomShowUser(docData: QueryDocumentSnapshot): RandomUserInfo {
        val email = docData.id
        val userName = docData.get("userName").toString()
        val about = docData.get("about").toString()
        val age = docData.get("age").toString().toInt()
        return RandomUserInfo(email, userName, age, about)
    }

}