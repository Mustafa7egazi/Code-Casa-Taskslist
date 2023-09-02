package com.example.codecasaquizapp.pojo

data class QuizQuestionModel(
    val id:Int,
    val text : String,
    val choices:List<String>,
    val difficulty : String,
    val correctAnswer:String
)
