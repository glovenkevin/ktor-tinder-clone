package com.sc.coding.model.request

data class NotificationRequest(
    val emailTo: String = "",
    val title: String = "",
    val message: String = ""
)