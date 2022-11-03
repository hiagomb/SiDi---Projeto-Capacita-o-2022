package com.example.projetocapacitacao.listener

interface ApiListener<T>{

    fun onSuccess(result: T)
    fun onFailure(result: T)
}