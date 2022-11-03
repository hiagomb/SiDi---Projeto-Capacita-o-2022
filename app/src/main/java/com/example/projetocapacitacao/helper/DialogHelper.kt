package com.example.projetocapacitacao.helper

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

class DialogHelper private constructor(){

    companion object{
        fun <T> showDialog(title: String, message: String, context: Context, destinyClass: Class<T>){
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title).setMessage(message).setCancelable(false).
            setPositiveButton("OK", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    context.startActivity(Intent(context, destinyClass))
                }
            }).show()
        }
    }
}