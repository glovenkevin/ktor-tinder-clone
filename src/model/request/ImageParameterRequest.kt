package com.sc.coding.model.request

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class ImageParameterRequest(
    val email: String,
    val seq: String
)