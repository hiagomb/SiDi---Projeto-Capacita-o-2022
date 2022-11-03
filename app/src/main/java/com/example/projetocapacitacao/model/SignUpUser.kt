package com.example.projetocapacitacao.model

data class SignUpUser(val name: String,
                      val phonenumber: String,
                      val email: String,
                      val cpf: String,
                      val password: String,
                      val acceptTerms: Boolean) {
}