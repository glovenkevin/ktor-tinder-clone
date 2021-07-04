package com.sc.coding.model.entity

data class DataUserRandomShowEntity(
    var currentEmail: String = "",
    var startAfter: Int = 0,
    var whiteList: MutableList<String> = mutableListOf(),
    var blackList: MutableList<String> = mutableListOf()
)
