package com.sc.coding.model.request

data class UserAccountUpdate(
    val email: String = "",
    val firstName: String = "",
    val userName: String = "",
    val about: String = "",
    val passion: String = "",
    val jobTitle: String = "",
    val company: String = "",
    val school: String = "",
    val city: String = "",
    val gender: String = "",
    val birthDay: String = "",
    val token: String = ""
) {
    override fun toString(): String {
        return "UserAccountUpdate(email='$email', firstName='$firstName', userName='$userName', about='$about', passion='$passion', jobTitle='$jobTitle', company='$company', school='$school', city='$city', gender='$gender', birthDay='$birthDay', token='$token')"
    }
}
