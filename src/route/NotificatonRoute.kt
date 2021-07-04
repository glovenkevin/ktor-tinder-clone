package com.sc.coding.route

import com.sc.coding.model.request.NotificationRequest
import com.sc.coding.service.NotificationService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Route.notificationRoute() {

    val notificationService by di().instance<NotificationService>()

    route("/notification") {

        post("/send") {
            val param = call.receive<NotificationRequest>()
            val response = notificationService.sendNotification(param)
            call.respond(response)
        }

    }
}