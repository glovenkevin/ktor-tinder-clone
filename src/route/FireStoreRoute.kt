package com.sc.coding.route

import com.sc.coding.model.request.InsertImage
import com.sc.coding.model.request.ImageParameter
import com.sc.coding.model.response.Response
import com.sc.coding.service.FireStoreService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Route.fireStoreRoute() {

    val fireStoreService by di().instance<FireStoreService>()

    route("/fireStore") {

        post("/add") {
            val insertImage = call.receive<InsertImage>()
            val response = fireStoreService.insertData(insertImage)
            call.respond(response)
        }

        get ("/getImage/{email}/{seq}") {
            val email = call.parameters["email"] ?: return@get call.respond(
                Response(503, "Bad Request", "Data tidak lengkap")
            )
            val seq = call.parameters["seq"] ?: return@get call.respond(
                Response(503, "Bad Request", "Data tidak lengkap")
            )
            val imageByteArray = fireStoreService.getImage(email, seq)
            if (null != imageByteArray) {
                call.respondBytes(imageByteArray, ContentType.Image.PNG,
                    HttpStatusCode.OK)
            } else {
                call.respond(Response(200, "Image not found"))
            }
        }

        post("/remove") {
            val removeImage = call.receive<ImageParameter>()
            val response = fireStoreService.removeData(removeImage)
            call.respond(response)
        }

    }
}
