package com.sc.coding.route

import com.sc.coding.configuration.MessageInfo
import com.sc.coding.model.request.InsertImageRequest
import com.sc.coding.model.request.ImageParameterRequest
import com.sc.coding.model.response.Response
import com.sc.coding.service.FireStoreService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.slf4j.LoggerFactory

fun Route.fireStoreRoute() {

    val log = LoggerFactory.getLogger(this::class.java)
    val fireStoreService by di().instance<FireStoreService>()

    route("/fireStore") {

        post("/add") {
            try {
                val insertImage = call.receive<InsertImageRequest>()
                val response = fireStoreService.insertData(insertImage)
                call.respond(response)
            } catch (e: Exception) {
                log.error(e.localizedMessage)
                call.respond(Response(400, MessageInfo.MSG_ERR, "JSON tidak lengkap"))
            }
        }

        get ("/getImage/{email}/{seq}") {
            val email = call.parameters["email"] ?: return@get call.respond(
                Response(400, MessageInfo.MSG_ERR, "Data tidak lengkap")
            )
            val seq = call.parameters["seq"] ?: return@get call.respond(
                Response(400, MessageInfo.MSG_ERR, "Data tidak lengkap")
            )
            
            if (email.trim().isBlank() || seq.trim().isBlank()) {
                return@get call.respond(Response(400, MessageInfo.MSG_ERR, "Data tidak lengkap"))
            }
            
            val imageByteArray = fireStoreService.getImage(email, seq)
            if (null != imageByteArray) {
                call.respondBytes(imageByteArray, ContentType.Image.PNG,
                    HttpStatusCode.OK)
            } else {
                call.respond(Response(200, "Image not found"))
            }
        }

        post("/remove") {
            val removeImage = call.receive<ImageParameterRequest>()
            val response = fireStoreService.removeData(removeImage)
            call.respond(response)
        }

        get("/getListImage/{email}") {
            val email = call.parameters["email"] ?: return@get call.respond(
                Response(400, MessageInfo.MSG_ERR, "Data tidak lengkap")
            )
            val response = fireStoreService.getListImage(email)
            call.respond(response)
        }

    }
}
