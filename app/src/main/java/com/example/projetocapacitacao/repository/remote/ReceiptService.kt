package com.example.projetocapacitacao.repository.remote

import com.example.projetocapacitacao.model.GetReceiptResponseModel
import com.example.projetocapacitacao.model.Receipt
import com.example.projetocapacitacao.model.PostReceiptResponseModel
import com.example.projetocapacitacao.model.ReceiptXX
import retrofit2.Call
import retrofit2.http.*

interface ReceiptService {

    @POST("receipt")
    fun createReceipt(@Header("Authorization") token: String, @Body receipt: Receipt):
            Call<PostReceiptResponseModel>

    @GET("receipt")
    fun getReceipts(@Header("Authorization") token: String,
                    @Query("q") q: String? = null,
                    @Query("startDate") startDate: String? = null,
                    @Query("endDate") endDate: String? = null): Call<GetReceiptResponseModel>

    @GET("receipt/{receiptId}")
    fun getReceipt(@Path("receiptId") receiptId: String,
                   @Header("Authorization") token: String): Call<ReceiptXX>



}