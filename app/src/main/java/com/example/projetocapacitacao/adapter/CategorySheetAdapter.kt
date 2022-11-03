package com.example.projetocapacitacao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.helper.Constants
import com.example.projetocapacitacao.helper.CustomSharedPrefs
import com.example.projetocapacitacao.model.Category
import com.example.projetocapacitacao.model.CategoryReceiptPut
import com.example.projetocapacitacao.model.ReceiptXX
import com.example.projetocapacitacao.viewModel.ReceiptDetailsViewModel

class CategorySheetAdapter(private val viewModel: ReceiptDetailsViewModel,
                           private val receipt: ReceiptXX,
                           private val onItemClicked: (Category) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _list: List<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategorySheetViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.itemview_sheet, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategorySheetViewHolder -> {
                holder.bind(_list.get(position), onItemClicked, viewModel, receipt)
            }
        }
    }

    override fun getItemCount(): Int {
        return _list.size
    }

    fun setDataset(list: List<Category>) {
        _list = list
        notifyDataSetChanged()
    }


    class CategorySheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName = itemView.findViewById<TextView>(R.id.textView_nameCategory_sheet)
        private val switch = itemView.findViewById<Switch>(R.id.switch_sheet)

        fun bind(category: Category, onItemClicked: (Category) -> Unit,
                 viewModel: ReceiptDetailsViewModel, receipt: ReceiptXX) {
            categoryName.text = category.category
            switch.isChecked = false
            for(c in receipt.categories){
                if(c.equals(category.id)){
                    switch.isChecked = true

                }
            }
            switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    viewModel.addReceiptToCategory(receipt.id, CustomSharedPrefs.
                    read(Constants.SHARED_PREFS.TOKEN_KEY)!!,
                        CategoryReceiptPut(category.id, p1))
                }
            })

            itemView.setOnClickListener {
                onItemClicked(category)
            }
        }
    }
}