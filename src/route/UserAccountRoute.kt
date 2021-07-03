package com.sc.coding.route

import com.sc.coding.model.request.UserAccountUpdate
import com.sc.coding.service.UserAccountService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Route.userAccountRoute() {

    val userAccountService by di().instance<UserAccountService>()

    route("/userAccount") {
        post("update") {
            val userAccountUpdate = call.receive<UserAccountUpdate>()
            val response = userAccountService.insertOrUpdateInformation(userAccountUpdate)
            call.respond(response)
        }
    }
}