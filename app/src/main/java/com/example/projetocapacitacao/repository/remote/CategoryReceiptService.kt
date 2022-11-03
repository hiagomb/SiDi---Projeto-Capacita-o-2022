package com.example.projetocapacitacao.repository.remote

import com.example.projetocapacitacao.model.CategoryReceiptPut
import com.example.projetocapacitacao.model.CategoryReceiptResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryReceiptService {

    @PUT("categoryReceipt/{receiptId}")
    fun addCategoryToReceipt(@Path("receiptId") id: String,
                             @Header("Authorization") token: String,
                             @Body categoryReceiptPut: CategoryReceiptPut):
            Call<CategoryReceiptResponse>
}