package com.example.projetocapacitacao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.projetocapacitacao.helper.Constants
import java.io.Serializable

@Entity(tableName = Constants.SQLITE.TABLE_NAME)
class ReceiptXX: Serializable{

    @ColumnInfo
    lateinit var authentication: String

    @ColumnInfo
    lateinit var cardInfoBrand: String


    @Ignore
    lateinit var categories: List<String>

    @ColumnInfo
    var date: Int = 0

    @ColumnInfo(name= "id_remote")
    lateinit var id: String

    @ColumnInfo
    lateinit var idUser: String

    @ColumnInfo
    lateinit var merchantIcon: String

    @ColumnInfo
    lateinit var merchantImage: String

    @ColumnInfo
    lateinit var merchantName: String

    @ColumnInfo
    lateinit var message: String

    @ColumnInfo
    var paymentMethod: Int = 0

    @ColumnInfo
    var status: Int = 0

    @ColumnInfo
    var value: Int = 0

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var idLocal: Int = 0
}