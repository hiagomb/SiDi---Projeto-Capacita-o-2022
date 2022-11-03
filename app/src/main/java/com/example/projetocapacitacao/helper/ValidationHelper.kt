package com.example.projetocapacitacao.helper

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class ValidationHelper private constructor() {


    companion object{
        fun validateEmptyField(context: Context, vararg inputText: TextInputEditText): Boolean{
            for(input in inputText){
                if(input.text.toString().isEmpty()){
                    Toast.makeText(context, input.hint,
                        Toast.LENGTH_SHORT).show()
                    input.requestFocus()
                    return false
                }
            }
            return true
        }

        fun validateCount(context: Context, inputText: TextInputEditText, type: String): Boolean{
            if(inputText.text.toString().replace(".", "").replace
                    ("-", "").replace("(", "").
                replace(")", "").replace(" ", "").length != 11){
                Toast.makeText(context, "Please type a valid $type",
                    Toast.LENGTH_SHORT).show()
                inputText.requestFocus()
                return false
            }
            return true
        }

    }

}