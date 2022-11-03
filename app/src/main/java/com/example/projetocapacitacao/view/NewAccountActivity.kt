package com.example.projetocapacitacao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.databinding.ActivityNewAccountBinding
import com.example.projetocapacitacao.helper.*
import com.example.projetocapacitacao.viewModel.NewAccountViewModel
import com.redmadrobot.inputmask.MaskedTextChangedListener

class NewAccountActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityNewAccountBinding
    private lateinit var viewModel: NewAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Create new account"
        viewModel = ViewModelProvider(this).get(NewAccountViewModel::class.java)

        binding.buttonRegisterNewAccount.setOnClickListener(this)
        ProgressHelper.showProgress(false, binding.progressBarNewAccount, window)
        setObservers()
        setMasksFields()
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.button_register_newAccount){
            handleCreateUser()
        }
    }

    private fun setMasksFields(){
        CustomMasks.setMask(binding.editPhoneNewAccount, CustomMasks.MASKS.PHONE)
        CustomMasks.setMask(binding.editCpfNewAccount, CustomMasks.MASKS.CPF)
    }

    private fun handleCreateUser(){
        if(ValidationHelper.validateEmptyField(this, binding.editNameNewAccount,
                binding.editPhoneNewAccount, binding.editEmailNewAccount, binding.editCpfNewAccount,
                binding.editPasswordNewAccount, binding.editConfirmPasswordNewAccount)){

            if(ValidationHelper.validateCount(this, binding.editPhoneNewAccount,
                    "phone")){
                if(ValidationHelper.validateCount(this, binding.editCpfNewAccount,
                        "CPF")){
                    if(binding.editPasswordNewAccount.text.toString().trim().
                        equals(binding.editConfirmPasswordNewAccount.text.toString().trim())){
                        if(binding.switch1.isChecked){
                            viewModel.createUser(binding.editNameNewAccount.text.toString(),
                                binding.editPhoneNewAccount.text.toString(),
                                binding.editEmailNewAccount.text.toString().trim(),
                                binding.editCpfNewAccount.text.toString(),
                                binding.editPasswordNewAccount.text.toString().trim())
                        }else{
                            Toast.makeText(this, Constants.NEW_ACCOUNT.TERMS_OF_SERVICE,
                                Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, Constants.NEW_ACCOUNT.PASSWORD_CONFIRMATION,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }

    private fun setObservers(){
        viewModel.user.observe(this){
            Toast.makeText(this, it.resultMessage, Toast.LENGTH_SHORT).show()
            if(it.code == Constants.HTTP.CREATE_SUCCESS){
                DialogHelper.showDialog(Constants.NEW_ACCOUNT.USER_CREATED,
                    Constants.NEW_ACCOUNT.LOGIN_REDIRECT, this,
                    LoginActivity::class.java)
            }
        }

        viewModel.progress.observe(this){
            ProgressHelper.showProgress(it, binding.progressBarNewAccount, window)
        }
    }
}