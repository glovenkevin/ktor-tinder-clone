package com.sc.coding.route

import io.ktor.application.*
import io.ktor.routing.*

fun Route.apiGate() {
    route("/api/v1") {
        fireStoreRoute()
        userAccountRoute()
        notificationRoute()
    }
}

fun Application.registerRoute() {
    routing {
        apiGate()
        healthCheck()
    }
}

