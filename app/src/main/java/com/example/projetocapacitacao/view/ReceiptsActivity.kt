package com.example.projetocapacitacao.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.adapter.CategorySearchAdapter
import com.example.projetocapacitacao.adapter.ReceiptsAdapter
import com.example.projetocapacitacao.databinding.ActivityReceiptsBinding
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.helper.CustomSharedPrefs
import com.example.projetocapacitacao.helper.ProgressHelper
import com.example.projetocapacitacao.model.Category
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.viewModel.HomeViewModel
import com.example.projetocapacitacao.viewModel.ReceiptsViewModel
import java.text.SimpleDateFormat
import java.util.ArrayList

class ReceiptsActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityReceiptsBinding
    private lateinit var viewModel: ReceiptsViewModel
    private lateinit var adapter: ReceiptsAdapter
    private lateinit var fullList: ArrayList<ReceiptXX>
    private lateinit var searchList: ArrayList<ReceiptXX>
    private lateinit var categoryAdapter: CategorySearchAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var list: List<ReceiptXX>


    companion object{
        lateinit var category_class: Category
        var dateClicked = 0
        var hasDateInfo = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ReceiptsViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fullList = arrayListOf()
        searchList = arrayListOf()

        setInitialInfos()
        setCurrentDate()
        setObservers()
        ProgressHelper.showProgress(false, binding.progressBarReceipts, window)
        initRecyclerView()
        initCategoryRV()
        binding.imageViewNoData.visibility = View.GONE
        binding.textShowHideOptions.setOnClickListener(this)
        binding.showRecycler.setOnClickListener(this)
        binding.btnSearch.setOnClickListener(this)
        binding.editStartDate.setOnTouchListener(this)
        binding.editEndDate.setOnTouchListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                if(!newText!!.isEmpty()){
                    list.forEach{
                        if(it.merchantName.lowercase().contains(newText.lowercase())){
                            searchList.add(it)
                        }
                    }
                }else{
                    searchList.addAll(list)
                }
                adapter.setDataset(searchList)
                return true
            }

        })

        return true
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.textShowHideOptions){
            viewModel.setShowingOptions(!viewModel.showingOptions.value!!)
        }else if(p0.id == R.id.show_recycler){
            viewModel.setShowingRecyler(!viewModel.showingRecycler.value!!)
        }else if(p0.id == R.id.btn_search){
            handleSearch()
        }
    }

    override fun onTouch(p0: View, p1: MotionEvent): Boolean {
        if(p0.id == R.id.edit_start_date || p0.id == R.id.edit_end_date){
            dateClicked = p0.id
            if(p1.action == MotionEvent.ACTION_UP){
                val date = binding.editStartDate.text.toString()
                val day = date.substringBefore("/").toInt()
                val month = date.substringAfter("/").substringBefore("/").toInt()-1
                val year = date.substringAfterLast("/").toInt()
                DatePickerDialog(this, this, year, month, day).show()

                return true
            }
        }
        return false
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        if(dateClicked == R.id.edit_start_date){
            binding.editStartDate.setText("$day/${month+1}/$year")
        }else if(dateClicked == R.id.edit_end_date){
            binding.editEndDate.setText("$day/${month+1}/$year")
        }
    }

    private fun handleSearch(){
        var query: String? = null
        val list = categoryAdapter.getCheckeList()
        if(list.size == 1){
            query = list[0].id
        }else if(list.size > 1){
            query = ""
            query = list[0].category
        }else if(list.size > 1){
            for(c in list){
                query += c.id+"*"
            }
            query!!.dropLast(1)
        }


        viewModel.getReceipts(CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)!!, query,
            null, null)
        hasDateInfo = true
        val s = binding.editStartDate.text.toString().replace("/", "")

        viewModel.getReceipts(CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)!!, query,
            binding.editStartDate.text.toString().replace("/", ""),
            binding.editEndDate.text.toString().replace("/", ""))

    }

    private fun setCurrentDate(){
        binding.editStartDate.setText("${Constants.CURRENT_DATE.DAY}/" +
                "${Constants.CURRENT_DATE.MONTH}/${Constants.CURRENT_DATE.YEAR}")
        binding.editEndDate.setText("${Constants.CURRENT_DATE.DAY}/" +
                "${Constants.CURRENT_DATE.MONTH}/${Constants.CURRENT_DATE.YEAR}")
    }

    private fun initRecyclerView() {
        adapter = ReceiptsAdapter{receiptXX ->
            val intent = Intent(this, ReceiptDetailsActivity::class.java)
            intent.putExtra(Constants.GENERAL.RECEIPT_DETAIL, receiptXX as ReceiptXX)
            startActivity(intent)
        }
        binding.recyclerReceiptsReceipts.layoutManager = LinearLayoutManager(this)
        binding.recyclerReceiptsReceipts.adapter = adapter
    }

    private fun initCategoryRV(){
        categoryAdapter = CategorySearchAdapter {  }
        binding.recyclerCategorySearch.layoutManager = LinearLayoutManager(this)
        binding.recyclerCategorySearch.adapter = categoryAdapter
    }

    private fun setInitialInfos() {
        val token = CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)
        val category: Category
        if(intent.extras!= null){
            category = if (Build.VERSION.SDK_INT >= 33) {
                intent.getSerializableExtra(Constants.GENERAL.CATEGORY, Category::class.java)!!
            } else {
                intent.getSerializableExtra(Constants.GENERAL.CATEGORY)
            } as Category
            category_class = category
        }

        supportActionBar?.title = category_class.category
        binding.textShowHideOptions.visibility = View.GONE

        if(category_class.id.equals("0")){ // all receipts
            viewModel.getReceipts(token!!)
            binding.textShowHideOptions.visibility = View.VISIBLE
        }else if(category_class.id.equals("1")){// favorites
            viewModel.getAllFavorites()
        }else{
            viewModel.getReceipts(token!!, category_class.id)
        }

        homeViewModel.getCategories(CustomSharedPrefs.read(Constants.SHARED_PREFS.TOKEN_KEY)!!)
        hasDateInfo = false
    }

    private fun filterDates(list: List<ReceiptXX>): List<ReceiptXX>{
        val _list = mutableListOf<ReceiptXX>()
        val startDate =  SimpleDateFormat("dd/MM/yyyy").parse(binding.editStartDate.text.toString())
        val endDate = SimpleDateFormat("dd/MM/yyyy").parse(binding.editEndDate.text.toString())
        for (l in list){
            val lDate = SimpleDateFormat("ddMMyyyy").parse(l.date.toString())
            if(lDate.compareTo(startDate) >= 0 && lDate.compareTo(endDate) <= 0){
                _list.add(l)
            }
        }
        return _list
    }

    private fun setObservers() {
        viewModel.progress.observe(this){
            ProgressHelper.showProgress(it, binding.progressBarReceipts, window)
        }
        viewModel.receiptResponse.observe(this){
            list = it.receipts!!
            if(hasDateInfo){
                list = filterDates(list!!)
            }
            setVisibility(list!!)
            fullList.addAll(list)
            searchList.addAll(list)
        }
        viewModel.listFavorites.observe(this){
            list = it
            if(hasDateInfo){
                list = filterDates(list!!)
            }
            setVisibility(list!!)
            fullList.addAll(list)
            searchList.addAll(list)

        }


        viewModel.showingOptions.observe(this){
            if(it){
                binding.layoutOptions.visibility = View.VISIBLE
                binding.textShowHideOptions.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_baseline_arrow_drop_up_24, 0)
            }else{
                binding.layoutOptions.visibility = View.GONE
                binding.textShowHideOptions.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_baseline_arrow_drop_down_24, 0)
            }
        }

        viewModel.showingRecycler.observe(this){
            if(it){
                binding.recyclerCategorySearch.visibility = View.VISIBLE
                binding.showRecycler.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_baseline_arrow_drop_up_24, 0)
            }else{
                binding.recyclerCategorySearch.visibility = View.GONE
                binding.showRecycler.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_baseline_arrow_drop_down_24, 0)
            }
        }

        homeViewModel.categories.observe(this){
            categoryAdapter.setDataset(it.categories)
        }
    }

    private fun setVisibility(list: List<ReceiptXX>){
        if(list.size!! > 0){
            binding.recyclerReceiptsReceipts.visibility = View.VISIBLE
            binding.imageViewNoData.visibility = View.GONE
            adapter.setDataset(list)
            binding.textShowHideOptions.visibility = View.VISIBLE
        }else{
            binding.recyclerReceiptsReceipts.visibility = View.GONE
            binding.imageViewNoData.visibility = View.VISIBLE
            binding.textShowHideOptions.visibility = View.GONE
        }
    }



}