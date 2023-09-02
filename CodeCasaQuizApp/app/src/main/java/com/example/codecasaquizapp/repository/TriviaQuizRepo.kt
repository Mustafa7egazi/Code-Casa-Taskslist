package com.example.codecasaquizapp.repository

import com.example.codecasaquizapp.pojo.BaseModel
import com.example.codecasaquizapp.source.ApiService

class TriviaQuizRepo {

    suspend fun getSetOfQuestions():List<BaseModel>?{
        val response= ApiService.retrofitService.getSetOfQuestions()
        if (response.isSuccessful){
            return response.body()
        }
        return null
    }
}