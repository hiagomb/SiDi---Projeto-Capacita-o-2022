package com.example.projetocapacitacao.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.UserResponseModel
import com.example.projetocapacitacao.repository.UserRepository

class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val _login = MutableLiveData<UserResponseModel>()
    val login: LiveData<UserResponseModel> = _login
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    fun login(phoneEmail: String, password: String){
        _progress.value = true
        userRepository.login(phoneEmail, password, object : ApiListener<UserResponseModel>{
            override fun onSuccess(result: UserResponseModel) {
                _login.value = result
                _progress.value = false
            }

            override fun onFailure(result: UserResponseModel) {
                _login.value = result
                _progress.value = false
            }

        })
    }

}