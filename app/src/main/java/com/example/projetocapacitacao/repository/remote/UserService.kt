package com.example.projetocapacitacao.repository.remote

import com.example.projetocapacitacao.model.SignUpUser
import com.example.projetocapacitacao.model.UserResponseModel
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @POST("users/login")
    @FormUrlEncoded
    fun login(@Field("email") phoneEmail:String,
              @Field("pass") password: String): Call<UserResponseModel>

    //I have a boolean parameter here, because of that I developed in that way
    @POST("users")
    fun createUser(@Body signUpUser: SignUpUser): Call<UserResponseModel>

    @POST("users/resetPassword")
    @FormUrlEncoded
    fun resetPassword(@Field("email") email: String,
                      @Field("cpf") cpf: String,
                      @Field("phonenumber") phonenumber: String): Call<UserResponseModel>
}