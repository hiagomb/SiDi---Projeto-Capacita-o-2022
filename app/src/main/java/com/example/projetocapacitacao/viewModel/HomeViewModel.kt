package com.example.projetocapacitacao.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.CategoryResponseModel
import com.example.projetocapacitacao.model.GetReceiptResponseModel
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.repository.CategoryRepository
import com.example.projetocapacitacao.repository.ReceiptDetailsRepository
import com.example.projetocapacitacao.repository.ReceiptRepository

class HomeViewModel(application: Application) : AndroidViewModel(application){

    private val categoryRepository = CategoryRepository()
    private val _categories = MutableLiveData<CategoryResponseModel>()
    val categories: LiveData<CategoryResponseModel> = _categories
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    fun getCategories(token: String){
        _progress.value = true
        categoryRepository.getCategories(token, object : ApiListener<CategoryResponseModel>{
            override fun onSuccess(result: CategoryResponseModel) {
                _categories.value = result
                _progress.value = false
            }

            override fun onFailure(result: CategoryResponseModel) {
                _categories.value = result
                _progress.value = false
            }

        })
    }

    private val receiptRepository = ReceiptRepository()
    private val _receiptResponse = MutableLiveData<GetReceiptResponseModel>()
    val receiptResponse: LiveData<GetReceiptResponseModel> = _receiptResponse

    fun getReceipts(token: String, q: String? = null){
        _progress.value = true
        receiptRepository.getReceipts(token, q, null, null,
            object : ApiListener<GetReceiptResponseModel>{
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