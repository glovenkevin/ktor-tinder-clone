package com.sc.coding.UserAccountRouteTest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sc.coding.configuration.MessageInfo
import com.sc.coding.model.response.Response
import com.sc.coding.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetEmailRouteTest {

    private lateinit var email: String

    @Before
    fun prepareTes() {
        this.email = "tes@tes.com"
    }

    @Test
    fun testGetEmail() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/userAccount/$email").apply {
                Assert.assertEquals(HttpStatusCode.OK, response.status())
                val resultString = response.content.toString()
                val mapper = jacksonObjectMapper()
                val resp: Response = mapper.readValue(resultString)
                Assert.assertEquals(200, resp.code)
                Assert.assertEquals(MessageInfo.MSG_OK, resp.status)
            }
        }
    }

    @Test
    fun testNoParam() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/userAccount/ ").apply {
                Assert.assertEquals(HttpStatusCode.OK, response.status())
                val resultString = response.content.toString()
                val mapper = jacksonObjectMapper()
                val resp: Response = mapper.readValue(resultString)
                Assert.assertEquals(400, resp.code)
                Assert.assertEquals(MessageInfo.MSG_ERR, resp.status)
            }
        }
    }
}