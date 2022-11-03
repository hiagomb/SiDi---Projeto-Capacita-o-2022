package com.example.projetocapacitacao.model

data class CategoryResponseModel(
    val categories: List<Category>,
    val code: Int,
    val resultMessage: String
)