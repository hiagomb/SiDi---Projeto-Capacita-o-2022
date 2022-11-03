package com.example.projetocapacitacao.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.UserResponseModel
import com.example.projetocapacitacao.repository.UserRepository

class ForgotPasswordViewModel: ViewModel() {

    private val userRepository = UserRepository()
    private val _user = MutableLiveData<UserResponseModel>()
    val user: LiveData<UserResponseModel> = _user
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    fun resetPassword(email: String, cpf: String, phonenumber: String){
        _progress.value = true
        userRepository.resetPassword(email, cpf, phonenumber,
            object : ApiListener<UserResponseModel>{
            override fun onSuccess(result: UserResponseModel) {
                _user.value = result
                _progress.value = false
            }

            override fun onFailure(result: UserResponseModel) {
                _user.value = result
                _progress.value = false
            }

        })
    }

}