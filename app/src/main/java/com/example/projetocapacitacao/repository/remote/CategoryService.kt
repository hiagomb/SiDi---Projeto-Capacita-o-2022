package com.example.projetocapacitacao.repository.remote

import com.example.projetocapacitacao.model.CategoryResponseModel
import com.example.projetocapacitacao.model.CreateCategoryResponseModel
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    @GET("category")
    fun getCategory(@Header("Authorization") token: String): Call<CategoryResponseModel>

    @POST("category")
    @FormUrlEncoded
    fun createCategory(@Header("Authorization") token: String,
                       @Field("categoryName") categoryName: String):
            Call<CreateCategoryResponseModel>

}