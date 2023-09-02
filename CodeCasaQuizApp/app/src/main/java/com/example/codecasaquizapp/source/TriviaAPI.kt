package com.example.codecasaquizapp.source
import com.example.codecasaquizapp.pojo.BaseModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://the-trivia-api.com/v2/"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

var retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


 interface TriviaServices {
     @GET("questions")
     suspend fun getSetOfQuestions():Response<List<BaseModel>>
}

object ApiService {
    val retrofitService: TriviaServices by lazy {
        retrofit.create(TriviaServices::class.java)
    }
}