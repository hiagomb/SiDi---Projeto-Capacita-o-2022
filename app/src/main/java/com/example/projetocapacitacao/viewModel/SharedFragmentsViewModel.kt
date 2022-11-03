package com.example.projetocapacitacao.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetocapacitacao.listener.ApiListener
import com.example.projetocapacitacao.model.Receipt
import com.example.projetocapacitacao.model.PostReceiptResponseModel
import com.example.projetocapacitacao.repository.ReceiptRepository

class SharedFragmentsViewModel: ViewModel() {

    private val _receipt = MutableLiveData<Receipt>()
    val receipt: LiveData<Receipt> = _receipt
    private val _receiptDate = MutableLiveData<String>()
    val receiptDate: LiveData<String> = _receiptDate

    fun setReceipt(new_receipt: Receipt, receiptDate: String){
        _receipt.value = new_receipt
        _receiptDate.value = receiptDate
    }



    private val receiptRepository = ReceiptRepository()
    private val _receiptSendReturn = MutableLiveData<PostReceiptResponseModel>()
    val postReceiptSendReturn: LiveData<PostReceiptResponseModel> = _receiptSendReturn
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    fun createReceipt(receipt: Receipt){
        _progress.value = true
        receiptRepository.createReceipt(receipt, object : ApiListener<PostReceiptResponseModel> {
            override fun onSuccess(result: PostReceiptResponseModel) {
                _receiptSendReturn.value = result
                _progress.value = false
            }

            override fun onFailure(result: PostReceiptResponseModel) {
                _receiptSendReturn.value = result
                _progress.value = false
            }

        })
    }



}