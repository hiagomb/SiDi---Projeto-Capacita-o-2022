package com.example.projetocapacitacao.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.adapter.ViewPagerAdapter
import com.example.projetocapacitacao.databinding.FragmentDetailsBinding
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.helper.CustomViewPager
import com.example.projetocapacitacao.helper.ValidationHelper
import com.example.projetocapacitacao.model.Receipt
import com.example.projetocapacitacao.view.CameraActivity
import com.example.projetocapacitacao.view.NewReceiptActivity
import com.example.projetocapacitacao.viewModel.SharedFragmentsViewModel
import java.util.*


class DetailsFragment : Fragment(), View.OnClickListener, View.OnTouchListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: SharedFragmentsViewModel
    private lateinit var radiosList: List<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radiosList = listOf<RadioButton>(binding.radioDebitDetails, binding.radioCreditDetails,
            binding.radioCashDetails, binding.radioBoletoDetails, binding.radioPixDetails,
            binding.radioOtherDetails)
        binding.buttonNextDetails.setOnClickListener(this)
        binding.editUserToSendDetails.setOnTouchListener(this)
        binding.editDateTimeDetails.setOnTouchListener(this)
        setUpRadios()
        setCurrentDate()
        viewModel = ViewModelProvider(requireActivity()).get(SharedFragmentsViewModel::class.java)
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.button_next_Details) {
            handleNext()
        }else{
            for(r in radiosList){
                if(p0.id != r.id){
                    r.isChecked = false
                }else{

                }
            }
        }
    }

    override fun onTouch(p0: View, p1: MotionEvent): Boolean {
        val drawable_right = 2
        if(p0.id == R.id.edit_UserToSend_Details){
            if(p1.action == MotionEvent.ACTION_UP){
                if(p1.getRawX() >= (binding.editUserToSendDetails.right -
                            binding.editUserToSendDetails.
                            compoundDrawables[drawable_right].bounds.width())) {
                    val intent = Intent(requireContext(), CameraActivity::class.java)
                    getResult.launch(intent)
                    return true
                }
            }
        }else if(p0.id == R.id.edit_DateTime_Details){
            if(p1.action == MotionEvent.ACTION_UP){
                val date = binding.editDateTimeDetails.text.toString()
                val day = date.substringBefore("/").toInt()
                val month = date.substringAfter("/").substringBefore("/").toInt()-1
                val year = date.substringAfterLast("/").toInt()
                DatePickerDialog(requireContext(), this, year, month, day).show()
                return true
            }
        }
        return false
    }


    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding.editDateTimeDetails.setText("$day/${month+1}/$year")
    }

    private fun setCurrentDate(){
        binding.editDateTimeDetails.setText("${Constants.CURRENT_DATE.DAY}/" +
                "${Constants.CURRENT_DATE.MONTH}/${Constants.CURRENT_DATE.YEAR}")
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                val value = it.data?.getStringExtra(Constants.GENERAL.ID_USER)
                binding.editUserToSendDetails.setText(value)
                binding.editAmountDetalis.requestFocus()
            }
        }

    private fun handleNext(){
        if(ValidationHelper.validateEmptyField(requireContext(), binding.editMerchantName,
                binding.editAmountDetalis, binding.editDateTimeDetails,
                binding.editMessageDetails)){
            val viewPager: CustomViewPager = requireActivity().findViewById(R.id.viewPager)
            val value = binding.editAmountDetalis.text.toString().toInt()
            val status = 0 //active
            val paymentMethod = getPaymethMethod()
            val cardInfoBrand = "Master Card"
            val merchantName = binding.editMerchantName.text.toString()
            val message = binding.editMessageDetails.text.toString()
            val date = binding.editDateTimeDetails.text.toString().
                replace("/", "").toInt()
            val idUserToSend = binding.editUserToSendDetails.text.toString()
            viewModel.setReceipt(
                Receipt(value, status, paymentMethod, cardInfoBrand, merchantName,
                message, date, idUserToSend), binding.editDateTimeDetails.text.toString()
            )
            ViewPagerAdapter.changeCurrentPage(1, viewPager)
        }
    }

    private fun setUpRadios(){
        for(l in radiosList){
            l.setOnClickListener(this)
        }
    }

    private fun getPaymethMethod(): Int{
        var payment = 1;
        for(i in 0 until radiosList.size){
            if(radiosList[i].isChecked){
                payment = i + 1
            }
        }
        return payment
    }


}