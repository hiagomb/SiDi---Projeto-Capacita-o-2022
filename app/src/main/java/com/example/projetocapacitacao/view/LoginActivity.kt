package com.example.projetocapacitacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.databinding.ActivityLoginBinding
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.helper.CustomSharedPrefs
import com.example.projetocapacitacao.helper.ProgressHelper
import com.example.projetocapacitacao.helper.ValidationHelper
import com.example.projetocapacitacao.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        supportActionBar?.hide()

        //click events
        binding.buttonLoginLogin.setOnClickListener(this)
        binding.buttonNewAccountLogin.setOnClickListener(this)
        binding.textForgotPasswordLogin.setOnClickListener(this)

        setObservers()
        ProgressHelper.showProgress(false, binding.progressBarLogin, window)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.button_login_login -> handleLogin()
            R.id.button_newAccount_Login -> startActivity(Intent(
                this, NewAccountActivity::class.java))
            R.id.text_forgotPassword_login -> startActivity(Intent(
                this, ForgotPasswordActivity::class.java))
        }
    }

    private fun handleLogin(){
        if(ValidationHelper.validateEmptyField(this, binding.editPhoneEmailLogin,
                binding.editPasswordLogin)){
            viewModel.login(binding.editPhoneEmailLogin.text.toString().trim(),
                binding.editPasswordLogin.text.toString().trim())
        }

    }

    private fun setObservers(){
        viewModel.login.observe(this){
            if(it.code == Constants.HTTP.SUCCESS){
                val sharedPreferences = CustomSharedPrefs.getSharedPrefsInstance(this)
                CustomSharedPrefs.write(Constants.SHARED_PREFS.NAME_KEY,
                    viewModel.login.value!!.name)
                CustomSharedPrefs.write(Constants.SHARED_PREFS.TOKEN_KEY,
                    viewModel.login.value!!.token)
                CustomSharedPrefs.write(Constants.SHARED_PREFS.AVATAR_KEY,
                    viewModel.login.value!!.avatar)
                CustomSharedPrefs.write(Constants.SHARED_PREFS.ID_USER,
                    viewModel.login.value!!.idUser)

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, it.resultMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.progress.observe(this){
            ProgressHelper.showProgress(it, binding.progressBarLogin, window)
        }
    }


}