package com.sc.coding.model.request

data class RandomUserRequest(
    val age: Int = 0,
    val gender: String = "",
    val userName: String = "",
    val email: String = "",
    val status: String = ""
)
