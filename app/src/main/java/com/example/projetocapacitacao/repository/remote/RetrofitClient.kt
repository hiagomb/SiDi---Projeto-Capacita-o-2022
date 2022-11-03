package com.example.projetocapacitacao.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitClient {


    companion object{

        private lateinit var instance: Retrofit
        private const val BASE_URL = "https://new-receipts-sidi-server.onrender.com/api/v2/"

        private fun getRetrofitInstance(): Retrofit{
            if(!Companion::instance.isInitialized){
                synchronized(RetrofitClient::class.java){
                    instance = Retrofit.Builder().
                    baseUrl(BASE_URL).
                    client(OkHttpClient.Builder().build()).
                    addConverterFactory(ScalarsConverterFactory.create()).
                    addConverterFactory(GsonConverterFactory.create()).
                    build()
                }
            }
            return instance
        }

        fun <T> getService(serviceClass: Class<T>): T{
            return getRetrofitInstance().create(serviceClass)
        }

    }









}