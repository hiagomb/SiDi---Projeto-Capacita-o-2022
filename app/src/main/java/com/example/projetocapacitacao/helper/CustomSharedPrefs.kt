package com.example.projetocapacitacao.helper

import android.content.Context
import android.content.SharedPreferences

class CustomSharedPrefs private constructor(){

    companion object{

        private lateinit var sharedPreferences: SharedPreferences
        fun getSharedPrefsInstance(context: Context): SharedPreferences{
            if(!Companion::sharedPreferences.isInitialized){
                sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS.SHAREDNAME,
                    Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }

        fun write(key: String, value: String){
            sharedPreferences.edit().putString(key, value).commit()
        }

        fun read(key: String) = sharedPreferences.getString(key, "EMPTY")

    }
}