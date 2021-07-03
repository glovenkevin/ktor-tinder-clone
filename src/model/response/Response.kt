package com.sc.coding.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response(
    var code: Int,
    var status: String,
    var data: Any? = null
)
