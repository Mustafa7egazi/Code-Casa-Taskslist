package com.example.codecasaquizapp.pojo


data class BaseModel (
	val category : String,
	val id : String,
	val correctAnswer : String,
	val incorrectAnswers : List<String>,
	val question : Question,
	val tags : List<String>,
	val type : String,
	val difficulty : String,
	val regions : List<String>,
	val isNiche : Boolean
)