package com.example.projetocapacitacao.view

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.adapter.CategorySheetAdapter
import com.example.projetocapacitacao.adapter.ReceiptsAdapter
import com.example.projetocapacitacao.databinding.ActivityReceiptDetailsBinding
import com.example.projetocapacitacao.databinding.CustomBottomSheetBinding
import com.example.projetocapacitacao.databinding.DialogCreateCategoryBinding
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.helper.CustomSharedPrefs
import com.example.projetocapacitacao.helper.ProgressHelper
import com.example.projetocapacitacao.helper.StringHelper
import com.example.projetocapacitacao.model.Category
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.viewModel.HomeViewModel
import com.example.projetocapacitacao.viewModel.ReceiptDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.io.File


class ReceiptDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityReceiptDetailsBinding
    private lateinit var viewModel: ReceiptDetailsViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var receiptXX_loc: ReceiptXX
    private lateinit var receiptXX_rem: ReceiptXX
    private var isFavorite: Boolean = false
    private lateinit var sheetBinding: CustomBottomSheetBinding
    private lateinit var dialogBinding: DialogCreateCategoryBinding
    private lateinit var adapter: CategorySheetAdapter
    private lateinit var categories: List<Category>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Receipt Details"
        viewModel = ViewModelProvider(this).get(ReceiptDetailsViewModel::class.java)
        setInitalInfo()
        setObservers()
        binding.textViewFavoriteDetails.setOnClickListener(this)
        binding.textViewCategoryDetails.setOnClickListener(this)

    }

    override fun onClick(p0: View) {
        if (p0.id == R.id.textView_favoriteDetails) {
            handleFavorite()
        } else if (p0.id == R.id.textView_categoryDetails) {
            showBottomSheet(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.share_receipt){
            handleShare()
        }
        return true
    }

    private fun handleShare(){

        val file: File = File(applicationContext.externalCacheDir,
            "Receipt - ${receiptXX_loc.merchantName}.txt")
        file.printWriter().use { out ->
            out.println("Merchant name: "+receiptXX_loc.merchantName)
            out.println("Amount: "+StringHelper.getFormattedCurrency(receiptXX_loc.value))
            out.println("Date: "+StringHelper.getFormattedDate(receiptXX_loc.date))
            out.println("Payment method: "+StringHelper.
                getPaymentString(receiptXX_loc.paymentMethod))
            out.println("Authentication code: "+receiptXX_loc.authentication)
        }

        val uri: Uri
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/*"
            uri = FileProvider.getUriForFile(applicationContext,
                applicationContext.packageName + ".provider", file)
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        val chooser = Intent.createChooser(sendIntent, "Share the receipt")
        for(res in packageManager.queryIntentActivities(chooser,
            PackageManager.MATCH_DEFAULT_ONLY)) {
            val packName = res.activityInfo.packageName
            this.grantUriPermission(packName, uri,  Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(chooser)
    }

    private fun handleFavorite() {
        viewModel.addOrRemoveToFavorites(receiptXX_loc, isFavorite)
    }

    private fun showBottomSheet(show: Boolean) {
        adapter = CategorySheetAdapter(viewModel, receiptXX_rem) { category ->

        }
        sheetBinding.recyclerViewSheet.layoutManager = LinearLayoutManager(this)
        sheetBinding.recyclerViewSheet.adapter = adapter
        getCategoriesList()
        sheetBinding.textViewSheetNewCategory.setOnClickListener {
            handleCreateCategory()
        }

        val dialog = BottomSheetDialog(this)
        ProgressHelper.showProgress(false, sheetBinding.progressBarBottomSheet, window)
        dialog.setContentView(sheetBinding.root)
        if(show)  dialog.show()
    }


    private fun getCategoriesList(){
        homeViewModel.getCategories(CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)!!)
    }

    private fun handleCreateCategory() {
        dialogBinding = DialogCreateCategoryBinding.inflate(layoutInflater)

        val dialog: AlertDialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setPositiveButton("OK", null).
            setNegativeButton("Cancel", null).create()

        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        positiveButton.setOnClickListener{
            if(dialogBinding.inputCreateCategory.text.toString().trim().isEmpty()){
                Toast.makeText(this, Constants.CATEGORY.CATEGORY_NAME,
                    Toast.LENGTH_SHORT).show()
            }else{
                val token = CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)
                viewModel.createCategory(token!!, dialogBinding.inputCreateCategory.text.toString())
                dialog.dismiss()
            }
        }
        negativeButton.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setInitalInfo() {
        if (Build.VERSION.SDK_INT >= 33) {
            receiptXX_loc = intent.getSerializableExtra(Constants.GENERAL.RECEIPT_DETAIL,
                ReceiptXX::class.java)!!
        } else{
            receiptXX_loc = intent.getSerializableExtra(Constants.GENERAL.RECEIPT_DETAIL)
                    as ReceiptXX
        }

        viewModel.getReceipt(receiptXX_loc.id)

        Glide.with(this).load(receiptXX_loc.merchantIcon).into(binding.imageViewRceiptDetails)
        binding.textViewCompanyDetails.text = receiptXX_loc.merchantName
        binding.textViewAuthDetails.text = "Auth code: " + receiptXX_loc.authentication
        binding.textViewDateDetails.text = StringHelper.getFormattedDate(receiptXX_loc.date)
        binding.textViewValueDetails.text = StringHelper.getFormattedCurrency(receiptXX_loc.value)
        binding.textViewPaymentDetails.text =
            "Payment Method: " + StringHelper.getPaymentString(receiptXX_loc.paymentMethod)

        viewModel.getReceiptFromServer(receiptXX_loc.id,
            CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)!!)

    }

    private fun setObservers() {
        viewModel.success.observe(this) {
            if (it) {
                if (isFavorite) {
                    isFavorite = false
                    setFavoriteIconColor(false)
                } else {
                    isFavorite = true
                    setFavoriteIconColor(true)
                }
            }
        }


        viewModel.receiptXX_local.observe(this) {//local (for favorites)
            if (it != null) {
                receiptXX_loc = it
                isFavorite = true
                setFavoriteIconColor(true)
            } else {
                isFavorite = false
                setFavoriteIconColor(false)
            }
        }

        viewModel.category.observe(this){
            getCategoriesList()
        }

        viewModel.receiptXX.observe(this){//remote
            receiptXX_rem = it
            showBottomSheet(false)
        }

        viewModel.progress.observe(this){
            sheetBinding = CustomBottomSheetBinding.inflate(
                layoutInflater, null,
                false
            )
            ProgressHelper.showProgress(it, sheetBinding.progressBarBottomSheet, window)
        }

        viewModel.categoryReceiptResponse.observe(this){
            if(it.code == Constants.HTTP.SUCCESS){
                viewModel.getReceiptFromServer(receiptXX_rem.id,
                    CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)!!)
            }
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.categories.observe(this){
            categories = it.categories
            adapter.setDataset(categories)
        }

    }

    private fun setFavoriteIconColor(isTurningFavorite: Boolean) {
        val color = if (isTurningFavorite) {
            Color.RED
        } else {
            Color.BLACK
        }
        binding.textViewFavoriteDetails.setTextColor(color)
        val drawable: Drawable? = AppCompatResources.getDrawable(
            this,
            R.drawable.ic_baseline_star_24
        )
        val wrappedDrawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, color)
        binding.textViewFavoriteDetails.setCompoundDrawablesWithIntrinsicBounds(
            null,
            drawable,
            null,
            null
        );
    }

}