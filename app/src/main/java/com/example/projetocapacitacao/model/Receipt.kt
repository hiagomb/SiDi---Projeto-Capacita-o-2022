package com.example.projetocapacitacao.model

data class Receipt(val value: Int, val status: Int, val paymentMethod: Int,
                   val cardInfoBrand: String, val merchantName: String, val message: String,
                   val date: Int, val idUserToSend: String) {
}