package com.sc.coding.FirestoreTest

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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FireStoreGetImageTest {

    private lateinit var email: String
    private lateinit var seq: String

    @Before
    fun prepareTest() {
        this.email = "tes@tes.com"
        this.seq = "1"
    }

    @Test
    fun testGetImage() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/fireStore/getImage/$email/$seq").apply {
                Assert.assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Image.PNG, response.contentType())
                assertNotNull(response.byteContent)
            }
        }
    }

    @Test
    fun testNoEmail() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/fireStore/getImage/ /$seq").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val resultString = response.content.toString()
                val mapper = jacksonObjectMapper()
                val resp: Response = mapper.readValue(resultString)
                Assert.assertEquals(400, resp.code)
                Assert.assertEquals(MessageInfo.MSG_ERR, resp.status)
            }
        }
    }

    @Test
    fun testNoSeq() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/fireStore/getImage/$email/ ").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val resultString = response.content.toString()
                val mapper = jacksonObjectMapper()
                val resp: Response = mapper.readValue(resultString)
                Assert.assertEquals(400, resp.code)
                Assert.assertEquals(MessageInfo.MSG_ERR, resp.status)
            }
        }
    }

    @Test
    fun testNoEmailAndSeq() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/fireStore/getImage/ / ").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val resultString = response.content.toString()
                val mapper = jacksonObjectMapper()
                val resp: Response = mapper.readValue(resultString)
                Assert.assertEquals(400, resp.code)
                Assert.assertEquals(MessageInfo.MSG_ERR, resp.status)
            }
        }
    }
}