package com.example.projetocapacitacao.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.databinding.ActivityForgotPasswordBinding
import com.example.projetocapacitacao.helper.*
import com.example.projetocapacitacao.viewModel.ForgotPasswordViewModel

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Forgot password"
        ProgressHelper.showProgress(false, binding.progressForgotPassword, window)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        binding.buttonVerificationForgot.setOnClickListener(this)
        setObservers()
        setMasksFields()
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.button_verification_forgot){
            handleForgotPassword()
        }
    }

    private fun setMasksFields(){
        CustomMasks.setMask(binding.editPhoneForgot, CustomMasks.MASKS.PHONE)
        CustomMasks.setMask(binding.editCpfForgot, CustomMasks.MASKS.CPF)
    }

    private fun handleForgotPassword(){
        if(ValidationHelper.validateEmptyField(this, binding.editPhoneForgot,
            binding.editEmailForgot, binding.editCpfForgot)){

            if(ValidationHelper.validateCount(this, binding.editPhoneForgot,
                    "phone")){
                if (ValidationHelper.validateCount(this, binding.editCpfForgot,
                        "CPF")){
                    viewModel.resetPassword(binding.editEmailForgot.text.toString().trim(),
                        binding.editCpfForgot.text.toString().trim(),
                        binding.editPhoneForgot.text.toString().trim())
                }
            }
        }
    }

    private fun setObservers(){
        viewModel.user.observe(this){
            Toast.makeText(this, it.resultMessage, Toast.LENGTH_SHORT).show()
            if(it.code == Constants.HTTP.CREATE_SUCCESS || it.code == Constants.HTTP.SUCCESS ){
                DialogHelper.showDialog(Constants.SUCCESS.PASSWORD_RESETED,
                    Constants.SUCCESS.EMAIL_INSTRUCTIONS, this,
                    LoginActivity::class.java)
            }
        }

        viewModel.progress.observe(this){
            ProgressHelper.showProgress(it, binding.progressForgotPassword, window)
        }
    }

}