package com.sc.coding.model.response

data class UserAccountInfoResponse(
    val userName: String,
    val firstName: String,
    val about: String,
    val passion: String,
    val jobTitle: String,
    val company: String,
    val school: String,
    val city: String,
    val gender: String,
    val birthDay: String
)
