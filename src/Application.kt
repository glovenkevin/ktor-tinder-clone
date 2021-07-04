package com.sc.coding

import com.fasterxml.jackson.databind.SerializationFeature
import com.sc.coding.configuration.FirebaseAdmin
import com.sc.coding.route.registerRoute
import com.sc.coding.service.bindServices
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.request.*
import org.kodein.di.ktor.di
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    HttpClient(CIO)

    install(CORS) {
        anyHost()
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    di {
        bindServices()
    }

    FirebaseAdmin.init()
    registerRoute()
}

