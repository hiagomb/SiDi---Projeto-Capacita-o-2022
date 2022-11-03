package com.example.projetocapacitacao.model

data class GetReceiptResponseModel(
    val code: Int,
    val receipts: List<ReceiptXX>?,
    val resultMessage: String
)