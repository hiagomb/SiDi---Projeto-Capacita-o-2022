package com.example.projetocapacitacao.model

data class ReceiptX(
    val authentication: String,
    val cardInfoBrand: String,
    val categories: List<String>,
    val date: Int,
    val id: String,
    val idUser: String,
    val merchantIcon: String,
    val merchantImage: String,
    val merchantName: String,
    val message: String,
    val paymentMethod: Int,
    val status: Int,
    val value: Int
)