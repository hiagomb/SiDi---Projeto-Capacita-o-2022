package com.example.projetocapacitacao.model

data class PostReceiptResponseModel(
    val code: Int,
    val receipt: ReceiptX?,
    val resultMessage: String
)