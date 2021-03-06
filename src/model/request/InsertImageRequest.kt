package com.sc.coding.model.request

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class InsertImageRequest(
    val email: String,
    val imageBase64: String,
    val seq: String
)
