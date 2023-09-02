package com.example.codecasaquizapp.viewmodel

import android.accounts.NetworkErrorException
import android.app.Application
import android.widget.Toast

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.codecasaquizapp.pojo.BaseModel
import com.example.codecasaquizapp.repository.TriviaQuizRepo
import com.example.codecasaquizapp.util.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class TriviaViewModel(private val application: Application) : AndroidViewModel(application) {

    private val repo = TriviaQuizRepo()

    private val _questionsDataResponse = MutableLiveData<List<BaseModel>?>()
    val questionsDataResponse: LiveData<List<BaseModel>?>
        get() = _questionsDataResponse

     val showPreviousBtn = MutableLiveData<Boolean>()

    init {
        showPreviousBtn.value = false
    }

    fun getSetOfQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isInternetAvailable(application.applicationContext)) {
                val result = repo.getSetOfQuestions()
                withContext(Dispatchers.Main) {
                    _questionsDataResponse.value = result
                }
            } else {
                throw IOException("No internet connection")
            }
        }
    }
}