package ru.awac.technical_aptitude_test.utils.retrofit

object Common {
    private val BASE_URL = "http://82.202.204.94/api-test/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}