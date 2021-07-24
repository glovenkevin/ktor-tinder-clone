package com.sc.coding.route

import com.sc.coding.configuration.MessageInfo
import com.sc.coding.model.request.RandomUserRequest
import com.sc.coding.model.entity.UserAccountInfoEntity
import com.sc.coding.model.response.Response
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

        get("/{email}") {
            val email = call.parameters["email"] ?: return@get call.respond(
                Response(400, MessageInfo.MSG_ERR, "Data tidak lengkap")
            )

            if (email.trim().isBlank()) return@get call.respond(
                Response(400, MessageInfo.MSG_ERR, "Data tidak lengkap")
            )

            val response = userAccountService.getUserAccountInfo(email)
            call.respond(response)
        }

        post("/update") {
            val userAccountUpdate = call.receive<UserAccountInfoEntity>()
            val response = userAccountService.insertOrUpdateInformation(userAccountUpdate)
            call.respond(response)
        }

        post("/getRandomUser") {
            val param = call.receive<RandomUserRequest>()
            val response = userAccountService.getRandomUser(param)
            call.respond(response)
        }

    }
}