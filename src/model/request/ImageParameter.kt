package com.sc.coding.model.request

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class ImageParameter(
    val email: String,
    val seq: String
)