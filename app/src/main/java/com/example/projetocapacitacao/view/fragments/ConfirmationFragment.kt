package com.example.projetocapacitacao.view.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.databinding.FragmentConfirmationBinding
import com.example.projetocapacitacao.helper.*
import com.example.projetocapacitacao.model.Receipt
import com.example.projetocapacitacao.view.HomeActivity
import com.example.projetocapacitacao.viewModel.SharedFragmentsViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ConfirmationFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentConfirmationBinding
    private lateinit var viewModel: SharedFragmentsViewModel
    private lateinit var receipt: Receipt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedFragmentsViewModel::class.java)
        binding.buttonSendConfirmation.setOnClickListener(this)
        setObservers()
        ProgressHelper.showProgress(false, binding.progressBarConfirmation,
            requireActivity().window)
    }


    override fun onClick(p0: View) {
        if(p0.id == R.id.button_Send_Confirmation){
            handleSend()
        }
    }

    private fun handleSend(){
        val sharedPreferences = CustomSharedPrefs.getSharedPrefsInstance(requireContext()) // DELETE HERE ?
        viewModel.createReceipt(receipt)
    }

    private fun updateFields(receipt: Receipt){
        binding.textUserConfirmation.text = receipt.idUserToSend
        binding.textAmountConfirmation.text = receipt.value.toString()
        binding.textMethodConfirmation.text = StringHelper.getPaymentString(receipt.paymentMethod)
        binding.textMessageConfirmation.text = receipt.message
    }

    private fun setObservers(){
        viewModel.receipt.observe(viewLifecycleOwner){
            receipt = it
            updateFields(receipt)
        }

        viewModel.receiptDate.observe(viewLifecycleOwner){
            binding.textDateConfirmation.text = it
        }

        viewModel.postReceiptSendReturn.observe(viewLifecycleOwner){
            if(it.code == Constants.HTTP.CREATE_SUCCESS){
                DialogHelper.showDialog(Constants.SUCCESS.TITLE_DIALOG,
                    Constants.SUCCESS.MESSAGE_DIALOG, requireContext(),
                    HomeActivity::class.java)
            }else{
                Toast.makeText(requireContext(), it.resultMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.progress.observe(viewLifecycleOwner){
            ProgressHelper.showProgress(it, binding.progressBarConfirmation,
                requireActivity().window)
        }
    }

}