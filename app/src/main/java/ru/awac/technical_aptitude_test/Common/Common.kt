package ru.awac.technical_aptitude_test.Common

import ru.awac.technical_aptitude_test.Interface.RetrofitServices
import ru.awac.technical_aptitude_test.Retrofit.RetrofitClient

object Common {
    private val BASE_URL = "http://82.202.204.94/api-test/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}