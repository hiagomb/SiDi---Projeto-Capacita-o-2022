package com.example.projetocapacitacao.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.model.Category

class CategorySearchAdapter(private val onItemClicked : (Category) -> Unit):
    RecyclerView.Adapter<CategorySearchAdapter.MyViewHolder>() {


    private var _list: List<Category> = ArrayList()
    private var checked_list = java.util.ArrayList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return CategorySearchAdapter.MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemview_categories_search, parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when(holder){
            is CategorySearchAdapter.MyViewHolder ->{
                holder.bind(_list.get(position), onItemClicked, checked_list)
                //click listener for recycler View
            }
        }
    }

    override fun getItemCount(): Int {
        return _list.size
    }


    fun setDataset(list: List<Category>){
        _list = list
        notifyDataSetChanged()
    }

    fun getCheckeList(): java.util.ArrayList<Category>{
        return checked_list
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val categoryName = itemView.findViewById<TextView>(R.id.textCategoryName)
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox_category)


        fun bind(category: Category, onItemClicked: (Category) -> Unit,
                 checked_list: java.util.ArrayList<Category>){
            categoryName.text = category.category
            checkBox.isChecked = false

            checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        checked_list.add(category)
                    }else{
                        checked_list.remove(category)
                    }
                }

            })

            itemView.setOnClickListener{
                onItemClicked(category)
            }
        }

    }

}