package ru.awac.technical_aptitude_test.Model

data class PaymentsModel(
    var success: Boolean? = null,
    var response: List<ResponseModel>? = null,
    var error: ErrorModel? = null
)
