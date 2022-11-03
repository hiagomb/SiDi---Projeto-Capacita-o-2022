package com.example.projetocapacitacao.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.adapter.CategoryHomeAdapter
import com.example.projetocapacitacao.databinding.ActivityHomeBinding
import com.example.projetocapacitacao.databinding.DialogQrcodeBinding
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.helper.CustomSharedPrefs
import com.example.projetocapacitacao.helper.ProgressHelper
import com.example.projetocapacitacao.model.Category
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.viewModel.HomeViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryHomeAdapter
    private var categories = mutableListOf<Category>()
    private var favorites: List<ReceiptXX> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setInitialInfos()
        setObservers()
        binding.fabHome.setOnClickListener(this)
        binding.imageQrcodeHome.setOnClickListener(this)
        ProgressHelper.showProgress(false, binding.progressBarHome, window)
        initRecyclerView()
        binding.swipe.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                setInitialInfos()
            }

        })
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.fabHome){
            startActivity(Intent(this, NewReceiptActivity::class.java))
        }else if(p0.id == R.id.imageQrcode_Home){
            handleGenerateQrCode()
        }
    }

    private fun handleGenerateQrCode(){
        try{
            ProgressHelper.showProgress(true, binding.progressBarHome, window)
            val writter = QRCodeWriter()
            val bitMatrix = writter.encode(CustomSharedPrefs.read(Constants.SHARED_PREFS.ID_USER),
                BarcodeFormat.QR_CODE,
                512, 512)
            val bmp = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
            for(x in 0 until bitMatrix.width){
                for(y in 0 until bitMatrix.height){
                    bmp.setPixel(x, y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            val dialogBinding = DialogQrcodeBinding.inflate(layoutInflater)
            dialogBinding.imageViewQrCodeDialog.setImageBitmap(bmp)
            val dialog: AlertDialog = AlertDialog.Builder(this).setView(dialogBinding.root)
                .setPositiveButton("Close", null).create()
            dialog.show()
            ProgressHelper.showProgress(false, binding.progressBarHome, window)
        }catch(ex: WriterException){
            ex.printStackTrace()
        }
    }

    private fun initRecyclerView() {
        categoryAdapter = CategoryHomeAdapter { category: Category ->
            val intent = Intent(this, ReceiptsActivity::class.java)
            intent.putExtra(Constants.GENERAL.CATEGORY, category)
            startActivity(intent)
        }
        binding.recyclerCategoryHome.layoutManager = LinearLayoutManager(this)
        binding.recyclerCategoryHome.adapter = categoryAdapter
    }

    private fun setInitialInfos(){
        val sharedPreferences = CustomSharedPrefs.getSharedPrefsInstance(this)
        val name = CustomSharedPrefs.read(Constants.SHARED_PREFS.NAME_KEY)
        val avatar = CustomSharedPrefs.read(Constants.SHARED_PREFS.AVATAR_KEY)
        val token = CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)

        categories.clear()
        binding.textNameHome.text = "Hi, ${name}"
        Glide.with(this).load(avatar).into(binding.imgAvatarHome)
        viewModel.getReceipts(token!!)
        viewModel.getAllFavorites()
        viewModel.getCategories(token!!)


    }

    private fun setObservers(){
        viewModel.receiptResponse.observe(this){
            categories.add(Category(Constants.RECEIPTS.ALL_RECEIPTS,
                "white", "0", it.receipts!!.size))
            categoryAdapter.setDataset(categories)
        }

        viewModel.listFavorites.observe(this){
            favorites = it
            categories.add(Category(Constants.RECEIPTS.FAVORITES,
                "white", "1", favorites.size))
            categoryAdapter.setDataset(categories)
        }

        viewModel.categories.observe(this){
            for(i in it.categories){
                categories.add(i)
            }
            categoryAdapter.setDataset(categories)
            Toast.makeText(applicationContext, "Refreshed", Toast.LENGTH_SHORT).show()
            binding.swipe.isRefreshing = false
        }

        viewModel.progress.observe(this){
            ProgressHelper.showProgress(it, binding.progressBarHome, window)
        }
    }




}