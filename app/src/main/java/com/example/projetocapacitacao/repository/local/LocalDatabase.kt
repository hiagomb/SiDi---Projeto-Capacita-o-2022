package com.example.projetocapacitacao.repository.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.model.ReceiptXX


@Database(entities = [ReceiptXX::class], version = Constants.SQLITE.VERSION)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getDaoService(): FavoritesDAO

    companion object{

        private lateinit var instance: LocalDatabase

        fun getDatabase(context: Context): LocalDatabase{
            if(!::instance.isInitialized){
                synchronized(LocalDatabase::class){
                    instance = Room.databaseBuilder(context, LocalDatabase::class.java,
                        Constants.SQLITE.DATABASE_NAME).allowMainThreadQueries().build()
                }
            }
            return instance
        }

    }
}