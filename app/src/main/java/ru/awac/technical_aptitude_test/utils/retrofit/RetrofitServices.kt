package ru.awac.technical_aptitude_test.utils.retrofit

import retrofit2.Call
import retrofit2.http.*
import ru.awac.technical_aptitude_test.Model.LoginModel
import ru.awac.technical_aptitude_test.Model.PaymentsModel

interface RetrofitServices {
    @Headers("app-key: 12345","v: 1")
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): LoginModel

    @Headers("app-key: 12345","v: 1")
    @GET("payments")
    suspend fun getPayments(
        @Query("token") token: String
    ): PaymentsModel
}