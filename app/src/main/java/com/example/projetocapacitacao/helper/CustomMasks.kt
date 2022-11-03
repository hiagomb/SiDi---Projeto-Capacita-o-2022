package com.example.projetocapacitacao.helper

import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener

class CustomMasks private constructor() {

    companion object{
        fun setMask(inputEditText: TextInputEditText, maskType: String){
            val listener = MaskedTextChangedListener(maskType,
                inputEditText)
            inputEditText.addTextChangedListener(listener)
            inputEditText.onFocusChangeListener = listener
        }
    }

    object MASKS{
        const val PHONE = "([00]) [00000]-[0000]"
        const val CPF = "[000].[000].[000]-[00]"
    }

}