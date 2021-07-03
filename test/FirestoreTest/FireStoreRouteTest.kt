package com.sc.coding.FirestoreTest

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test

class FireStoreRouteTest {

    @Test
    fun insertImage() {
        withTestApplication {
            handleRequest(HttpMethod.Post, "/api/v1/fireStore/add") {

            }
        }
    }

}