package com.example.projetocapacitacao.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserResponseModel: Serializable {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("resultMessage")
    var resultMessage = ""

    @SerializedName("token")
    var token = ""

    @SerializedName("idUser")
    var idUser = ""

    @SerializedName("name")
    var name = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("cpf")
    var cpf = ""

    @SerializedName("avatar")
    var avatar = ""
}