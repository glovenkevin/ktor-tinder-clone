package com.sc.coding

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sc.coding.model.response.Response
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/healthCheck").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val resultString = response.content.toString()
                val mapper = jacksonObjectMapper()
                val response: Response = mapper.readValue(resultString)
                assertEquals("OK", response.status)
            }
        }
    }
}
