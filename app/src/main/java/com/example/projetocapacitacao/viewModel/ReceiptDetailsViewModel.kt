package com.example.projetocapacitacao.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.*
import com.example.projetocapacitacao.repository.CategoryRepository
import com.example.projetocapacitacao.repository.ReceiptDetailsRepository
import com.example.projetocapacitacao.repository.ReceiptRepository

class ReceiptDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val receiptDetailsRepository = ReceiptDetailsRepository(application.applicationContext)
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success
    private val _receiptXX = MutableLiveData<ReceiptXX>()
    val receiptXX: LiveData<ReceiptXX> = _receiptXX
    private val categoryRepository = CategoryRepository()
    private val _category = MutableLiveData<CreateCategoryResponseModel>()
    val category: LiveData<CreateCategoryResponseModel> = _category
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress


    fun createCategory(token: String, name: String){
        _progress.value = true
        categoryRepository.createCategory(token, name, object : ApiListener<CreateCategoryResponseModel>{
            override fun onSuccess(result: CreateCategoryResponseModel) {
                _category.value = result
                _progress.value = false
            }

            override fun onFailure(result: CreateCategoryResponseModel) {
                _category.value = result
                _progress.value = false
            }

        })
    }

    private val _categoryReceiptResponse = MutableLiveData<CategoryReceiptResponse>()
    val categoryReceiptResponse: LiveData<CategoryReceiptResponse> = _categoryReceiptResponse

    fun addReceiptToCategory(receiptId: String, token: String,
                             categoryReceiptPut: CategoryReceiptPut){
        _progress.value = true
        categoryRepository.addCategoryToReceipt(receiptId, token, categoryReceiptPut, object : ApiListener<CategoryReceiptResponse>{
            override fun onSuccess(result: CategoryReceiptResponse) {
                _categoryReceiptResponse.value = result
                _progress.value = false
            }

            override fun onFailure(result: CategoryReceiptResponse) {
                _categoryReceiptResponse.value = result
                _progress.value = false
            }

        })
    }

    fun addOrRemoveToFavorites(receiptXX: ReceiptXX, isFavorite: Boolean){
        if(isFavorite){
            _success.value = receiptDetailsRepository.delete(receiptXX.id)
        }else{
            _success.value = receiptDetailsRepository.insert(receiptXX)
        }
    }


    private val _receiptXX_local = MutableLiveData<ReceiptXX>()
    val receiptXX_local: LiveData<ReceiptXX> = _receiptXX_local

    fun getReceipt(id_remote: String){
        _receiptXX_local.value = receiptDetailsRepository.get(id_remote)
    }



    private val receiptRepository = ReceiptRepository()

    fun getReceiptFromServer(receiptId: String, token: String){
        _progress.value = true
        receiptRepository.getReceipt(receiptId, token, object : ApiListener<ReceiptXX>{
            override fun onSuccess(result: ReceiptXX) {
                _receiptXX.value = result
                _progress.value = false
            }

            override fun onFailure(result: ReceiptXX) {
                _receiptXX.value = result
                _progress.value = false
            }

        })
    }

}