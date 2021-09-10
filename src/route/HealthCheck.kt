package com.sc.coding.route

import com.sc.coding.model.response.Response
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.healthCheck() {

    route("/health-check") {
        get {
            call.respond(Response(200, "OK"))
        }
    }

}