package com.sc.coding.route

import com.sc.coding.model.request.InsertImage
import com.sc.coding.service.FireStoreService
import io.ktor.application.*
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

    }
}
