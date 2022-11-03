package com.example.projetocapacitacao.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.model.ReceiptXX

@Dao
interface FavoritesDAO {

    @Insert
    fun insert(receiptXX: ReceiptXX): Long

    @Query("delete from "+Constants.SQLITE.TABLE_NAME+" where id_remote = :id_remote")
    fun delete(id_remote: String): Int

    @Query("select * from "+Constants.SQLITE.TABLE_NAME+"")
    fun getAll(): List<ReceiptXX>

    @Query("select * from "+Constants.SQLITE.TABLE_NAME+" where id_remote = :id_remote")
    fun getReceipt(id_remote: String): ReceiptXX?

}