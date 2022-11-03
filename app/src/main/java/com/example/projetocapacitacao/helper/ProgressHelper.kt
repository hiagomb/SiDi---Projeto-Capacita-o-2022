package com.example.projetocapacitacao.helper

import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar

class ProgressHelper private constructor(){

    companion object{
        fun showProgress(boolean: Boolean, progressBar: ProgressBar, window: Window){
            if(boolean){
                progressBar.visibility = View.VISIBLE
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }else{
                progressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

}