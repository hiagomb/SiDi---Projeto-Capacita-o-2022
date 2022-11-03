package com.example.projetocapacitacao.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.GetReceiptResponseModel
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.repository.ReceiptDetailsRepository
import com.example.projetocapacitacao.repository.ReceiptRepository

class ReceiptsViewModel(application: Application) : AndroidViewModel(application) {


    private val receiptRepository = ReceiptRepository()
    private val _receiptResponse = MutableLiveData<GetReceiptResponseModel>()
    val receiptResponse: LiveData<GetReceiptResponseModel> = _receiptResponse
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress
    private val _showingOptions = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    val showingOptions: LiveData<Boolean> = _showingOptions

    private val _showingRecycler = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    val showingRecycler: LiveData<Boolean> = _showingRecycler


    fun setShowingOptions(showing: Boolean){
        _showingOptions.value = showing
    }

    fun setShowingRecyler(showing: Boolean){
        _showingRecycler.value = showing
    }

    fun getReceipts(token: String, q: String? = null, startDate: String? = null,
                    endDate: String? = null){
        _progress.value = true
        receiptRepository.getReceipts(token, q, startDate, endDate,
            object : ApiListener<GetReceiptResponseModel> {
            override fun onSuccess(result: GetReceiptResponseModel) {
                _receiptResponse.value = result
                _progress.value = false
            }

            override fun onFailure(result: GetReceiptResponseModel) {
                _receiptResponse.value = result
                _progress.value = false
            }

        })
    }


    private val receiptDetailsRepository = ReceiptDetailsRepository(application.applicationContext)
    private val _listFavorites = MutableLiveData<List<ReceiptXX>>()
    val listFavorites: LiveData<List<ReceiptXX>> = _listFavorites

    fun getAllFavorites(){
        _listFavorites.value = receiptDetailsRepository.getAll()
    }
}