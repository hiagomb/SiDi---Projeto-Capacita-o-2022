package com.example.projetocapacitacao.repository

import android.content.Context
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.repository.local.LocalDatabase

class ReceiptDetailsRepository(private val context: Context) {

    private val local = LocalDatabase.getDatabase(context).getDaoService()

    fun insert(receiptXX: ReceiptXX): Boolean{
        return local.insert(receiptXX) > 0
    }

    fun delete(id_remote: String): Boolean{
        return local.delete(id_remote) > 0
    }

    fun get(id_remote: String): ReceiptXX? {
        return local.getReceipt(id_remote)
    }

    fun getAll(): List<ReceiptXX>{
        return local.getAll()
    }

}