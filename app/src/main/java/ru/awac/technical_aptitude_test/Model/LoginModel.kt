package ru.awac.technical_aptitude_test.Model

data class LoginModel(
    var success: Boolean? = null,
    var response: ResponseModel? = null,
    var error: ErrorModel? = null

)
