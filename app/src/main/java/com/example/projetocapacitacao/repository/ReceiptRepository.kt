package com.example.projetocapacitacao.repository

import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.helper.CustomSharedPrefs
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.GetReceiptResponseModel
import com.example.projetocapacitacao.model.Receipt
import com.example.projetocapacitacao.model.PostReceiptResponseModel
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.repository.remote.ReceiptService
import com.example.projetocapacitacao.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptRepository {

    private val remote = RetrofitClient.getService(ReceiptService::class.java)

    fun createReceipt(receipt: Receipt, apiListener: ApiListener<PostReceiptResponseModel>){
        val token = CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)
        val call = remote.createReceipt(Constants.HTTP.BEARER + token, receipt)
        var postReceiptResponseModel: PostReceiptResponseModel
        call.enqueue(object : Callback<PostReceiptResponseModel>{
            override fun onResponse(call: Call<PostReceiptResponseModel>,
                                    response: Response<PostReceiptResponseModel>) {
                if(response.code() == Constants.HTTP.SUCCESS ||
                    response.code() == Constants.HTTP.CREATE_SUCCESS){
                    response.body()?.let { apiListener.onSuccess(it) }
                }else{
                    postReceiptResponseModel = Gson().fromJson(response.errorBody()!!.string(),
                        PostReceiptResponseModel::class.java)
                    apiListener.onFailure(postReceiptResponseModel)
                }
            }

            override fun onFailure(call: Call<PostReceiptResponseModel>, t: Throwable) {
                postReceiptResponseModel = PostReceiptResponseModel(0, null,
                    Constants.HTTP.UNEXPECTED_ERROR)
                apiListener.onFailure(postReceiptResponseModel)
            }

        })

    }

    fun getReceipts(token: String, q: String? = null, startDate: String? = null,
                    endDate: String? = null, listener: ApiListener<GetReceiptResponseModel>){
        val call = remote.getReceipts(Constants.HTTP.BEARER + token, q, startDate, endDate)
        var getReceiptResponseModel: GetReceiptResponseModel
        call.enqueue(object : Callback<GetReceiptResponseModel>{
            override fun onResponse(call: Call<GetReceiptResponseModel>,
                response: Response<GetReceiptResponseModel>) {
                if(response.code() == Constants.HTTP.SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                }else{
                    getReceiptResponseModel = Gson().fromJson(response.errorBody()!!.string(),
                        GetReceiptResponseModel::class.java)
                    listener.onFailure(getReceiptResponseModel)
                }
            }

            override fun onFailure(call: Call<GetReceiptResponseModel>, t: Throwable) {
                getReceiptResponseModel = GetReceiptResponseModel(0, null,
                    Constants.HTTP.UNEXPECTED_ERROR)
                listener.onFailure(getReceiptResponseModel)
            }

        })
    }

    fun getReceipt(receiptId: String, token: String, listener: ApiListener<ReceiptXX>){
        val call = remote.getReceipt(receiptId, Constants.HTTP.BEARER + token)
        var receiptXX: ReceiptXX
        call.enqueue(object : Callback<ReceiptXX>{
            override fun onResponse(call: Call<ReceiptXX>, response: Response<ReceiptXX>) {
                if(response.code() == Constants.HTTP.SUCCESS){
                    response.body()?.let { listener.onSuccess(it) }
                }else{
                    receiptXX = Gson().fromJson(response.errorBody()!!.string(),
                        ReceiptXX::class.java)
                    listener.onFailure(receiptXX)
                }
            }

            override fun onFailure(call: Call<ReceiptXX>, t: Throwable) {
                receiptXX = ReceiptXX()
                receiptXX.message = Constants.HTTP.UNEXPECTED_ERROR
                listener.onFailure(receiptXX)
            }

        })
    }


}