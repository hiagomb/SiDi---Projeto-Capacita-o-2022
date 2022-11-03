package com.example.projetocapacitacao.repository

import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.CategoryReceiptPut
import com.example.projetocapacitacao.model.CategoryReceiptResponse
import com.example.projetocapacitacao.model.CategoryResponseModel
import com.example.projetocapacitacao.model.CreateCategoryResponseModel
import com.example.projetocapacitacao.repository.remote.CategoryReceiptService
import com.example.projetocapacitacao.repository.remote.CategoryService
import com.example.projetocapacitacao.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepository {

    private val remote = RetrofitClient.getService(CategoryService::class.java)

    fun getCategories(token: String, listener: ApiListener<CategoryResponseModel>) {
        val call = remote.getCategory(Constants.HTTP.BEARER + token)
        commonCall(call, listener)
    }

    private val categoryReceipt_remote = RetrofitClient.
        getService(CategoryReceiptService::class.java)

    fun addCategoryToReceipt(receiptId: String, token: String,
                             categoryReceiptPut: CategoryReceiptPut,
                             listener: ApiListener<CategoryReceiptResponse>){
        val call = categoryReceipt_remote.addCategoryToReceipt(receiptId,Constants.HTTP.BEARER
                + token, categoryReceiptPut)

        var categoryReceipt: CategoryReceiptResponse
        call.enqueue(object : Callback<CategoryReceiptResponse>{
            override fun onResponse(call: Call<CategoryReceiptResponse>,
                response: Response<CategoryReceiptResponse>) {
                if (response.code() == Constants.HTTP.SUCCESS ||
                    response.code() == Constants.HTTP.CREATE_SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                }else{
                    categoryReceipt = Gson().fromJson(response.errorBody()!!.string(),
                        CategoryReceiptResponse::class.java)
                    listener.onFailure(categoryReceipt)
                }
            }

            override fun onFailure(call: Call<CategoryReceiptResponse>, t: Throwable) {
                categoryReceipt = CategoryReceiptResponse(404, Constants.HTTP.UNEXPECTED_ERROR)
                listener.onFailure(categoryReceipt)
            }

        })
    }

    fun createCategory(token: String, name: String,
                       listener: ApiListener<CreateCategoryResponseModel>) {
        val call = remote.createCategory(Constants.HTTP.BEARER + token, name)
        var category: CreateCategoryResponseModel
        call.enqueue(object : Callback<CreateCategoryResponseModel>{
            override fun onResponse(call: Call<CreateCategoryResponseModel>,
                response: Response<CreateCategoryResponseModel>) {
                if (response.code() == Constants.HTTP.SUCCESS ||
                    response.code() == Constants.HTTP.CREATE_SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                }else{
                    category = Gson().fromJson(response.errorBody()!!.string(),
                        CreateCategoryResponseModel::class.java)
                    listener.onFailure(category)
                }
            }

            override fun onFailure(call: Call<CreateCategoryResponseModel>, t: Throwable) {
                category = CreateCategoryResponseModel(404, null,
                    "Unexpected error")
                listener.onFailure(category)
            }

        })
    }

    private fun commonCall(
        call: Call<CategoryResponseModel>,
        listener: ApiListener<CategoryResponseModel>
    ) {
        var category: CategoryResponseModel
        call.enqueue(object : Callback<CategoryResponseModel> {
            override fun onResponse(
                call: Call<CategoryResponseModel>,
                response: Response<CategoryResponseModel>
            ) {
                if (response.code() == Constants.HTTP.SUCCESS ||
                    response.code() == Constants.HTTP.CREATE_SUCCESS
                ) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    category = Gson().fromJson(
                        response.errorBody()!!.string(),
                        CategoryResponseModel::class.java
                    )
                    listener.onFailure(category)
                }
            }

            override fun onFailure(call: Call<CategoryResponseModel>, t: Throwable) {
                category = CategoryResponseModel(listOf(), 0, "Unexpected error")
                listener.onFailure(category)
            }
        })
    }
}