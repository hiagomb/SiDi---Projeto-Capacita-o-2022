package com.example.projetocapacitacao.repository

import android.content.Context
import android.widget.ProgressBar
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.SignUpUser
import com.example.projetocapacitacao.model.UserResponseModel
import com.example.projetocapacitacao.repository.remote.RetrofitClient
import com.example.projetocapacitacao.repository.remote.UserService
import com.example.projetocapacitacao.view.LoginActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private val remote = RetrofitClient.getService(UserService::class.java)

    fun login(phoneEmail: String, password: String, listener: ApiListener<UserResponseModel>){
        val call = remote.login(phoneEmail, password)
        commonCall(call, listener)
    }

    fun createUser(name: String, phone:String, email:String, cpf:String, password: String,
                   listener: ApiListener<UserResponseModel>){
        val signUpUser = SignUpUser(name, phone, email, cpf, password, true)
        val call = remote.createUser(signUpUser)
        commonCall(call, listener)
    }

    fun resetPassword(email: String, cpf: String, phonenumber: String,
                      listener: ApiListener<UserResponseModel>){
        val call = remote.resetPassword(email, cpf, phonenumber)
        commonCall(call, listener)
    }


    private fun commonCall(call: Call<UserResponseModel>, listener: ApiListener<UserResponseModel>){
        var userResponseModel = UserResponseModel()
        call.enqueue(object : Callback<UserResponseModel>{
            override fun onResponse(call: Call<UserResponseModel>,
                                    response: Response<UserResponseModel>) {
                if(response.code() == Constants.HTTP.SUCCESS ||
                    response.code() == Constants.HTTP.CREATE_SUCCESS){
                    response.body()?.let {
                        it.code = response.code()
                        listener.onSuccess(it)
                    }
                }else{
                    userResponseModel = Gson().fromJson(
                        response.errorBody()!!.string(), UserResponseModel::class.java)
                    listener.onFailure(userResponseModel)
                }
            }

            override fun onFailure(call: Call<UserResponseModel>, t: Throwable) {
                userResponseModel.code = 0
                userResponseModel.resultMessage = Constants.HTTP.UNEXPECTED_ERROR
                listener.onFailure(userResponseModel)
            }

        })
    }


}