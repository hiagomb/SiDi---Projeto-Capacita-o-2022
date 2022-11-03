package com.example.projetocapacitacao.helper

import java.text.NumberFormat

class StringHelper {

    companion object{
        fun getPaymentString(code: Int): String{
            var payment = when(code){
                1 -> "Debit Card"
                2 -> "Credit Card"
                3 -> "Money"
                4 -> "Payment Slip"
                5 -> "Pix"
                else -> "Other"
            }
            return payment
        }

        fun getFormattedCurrency(value: Int) = NumberFormat.
        getCurrencyInstance().format(value).replace(".", ",")

        fun getFormattedDate(date: Int) = date.toString().substringBefore("/")+
            "/"+date.toString().substringAfter("/").substringBeforeLast("/")+
                "/"+date.toString().substringAfterLast("/")


    }
}