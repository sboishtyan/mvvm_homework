package ru.awac.technical_aptitude_test.Interface

import retrofit2.Call
import retrofit2.http.*
import ru.awac.technical_aptitude_test.Model.ApiResponse

interface RetrofitServices {
    @Headers("app-key: 12345","v: 1")
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Call<ApiResponse>
}