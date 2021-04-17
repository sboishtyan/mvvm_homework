package ru.awac.technical_aptitude_test.Model

data class ResponseContainer(
    var token: String? = null,
    var desc: String? = null,
    var amount: Double? = null,
    var currency: String? = null,
    var created:  Long? = null
)
