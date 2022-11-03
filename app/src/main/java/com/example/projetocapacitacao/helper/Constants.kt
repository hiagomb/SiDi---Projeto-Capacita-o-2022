package com.example.projetocapacitacao.helper

import java.util.*

class Constants private constructor() {

    object HTTP{
        const val SUCCESS = 200
        const val CREATE_SUCCESS = 201
        const val BEARER = "Bearer "
        const val UNEXPECTED_ERROR = "Unexpected Error"
    }

    object GENERAL{
        const val ID_USER = "id_user"
        const val RECEIPT_DETAIL = "RECEIPT_DETAIL"
        const val CATEGORY = "category"
        const val FAVORITES = "favorites"
    }

    object SHARED_PREFS{
        const val SHAREDNAME = "SHARED_PROJETO_CAPACITACAO"
        const val NAME_KEY = "name_key"
        const val TOKEN_KEY = "token_key"
        const val AVATAR_KEY = "avatar_key"
        const val ID_USER = "id_user_key"
    }

    object SQLITE{
        const val DATABASE_NAME = "RECEIPTS_DATABASE"
        const val VERSION = 1
        const val TABLE_NAME = "FAVORITE_RECEIPTS"
    }

    object CURRENT_DATE{
        val DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val MONTH = Calendar.getInstance().get(Calendar.MONTH)+1
        val YEAR = Calendar.getInstance().get(Calendar.YEAR)
    }

    object SUCCESS{
        const val TITLE_DIALOG = "Receipt sent successfully!"
        const val MESSAGE_DIALOG = "Congratulations, you have created and exported the receipt" +
                " with success"
        const val PASSWORD_RESETED = "Password reseted"
        const val EMAIL_INSTRUCTIONS = "You will receive an email with instructions in order to " +
                "reset your password"
    }

    object CAMERA{
        const val PERMISSION_NEEDED = "You need the camera permission"
    }

    object RECEIPTS{
        const val NOT_RECEIPTS_YET = "You don´t have receipts in this category yet"
        const val ALL_RECEIPTS = "All receipts"
        const val FAVORITES = "Favorites"
    }

    object NEW_ACCOUNT{
        const val TERMS_OF_SERVICE = "You must agree with the Terms of Service in order to register"
        const val PASSWORD_CONFIRMATION = "The password and the confirmation must be equal"
        const val USER_CREATED = "User created with success"
        const val LOGIN_REDIRECT = "You will be redirected to the login page now!"
    }

    object FRAGMENTTABS{
        const val DETAILS= "Details"
        const val CONFIRMATION = "Confirmation"
    }

    object CATEGORY{
        const val CATEGORY_NAME = "Enter the category name"
    }


}