package ru.awac.technical_aptitude_test.Model

data class ApiResponse(
    var success: Boolean? = null,
    var response: ResponseContainer? = null,
    var error: ErrorContainer? = null

)
