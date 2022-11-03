package com.example.projetocapacitacao.model

import java.io.Serializable

data class Category(
    val category: String,
    val color: String,
    val id: String,
    val countReceipts: Int
): Serializable