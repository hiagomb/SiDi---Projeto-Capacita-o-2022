package com.example.projetocapacitacao.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.model.Category

class CategoryHomeAdapter(private val onItemClicked : (Category) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _list: List<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).
        inflate(R.layout.itemview_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CategoryViewHolder->{
                holder.bind(_list.get(position), onItemClicked)
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

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.textItemViewCategory_title)
        private val count = itemView.findViewById<TextView>(R.id.textItemViewCategory_count)
        private var cardView = itemView.findViewById<CardView>(R.id.cardView_category)

        fun bind(category: Category, onItemClicked: (Category) -> Unit){
            title.text = category.category
            count.text = category.countReceipts.toString()+" receipts"
            cardView.setCardBackgroundColor(Color.parseColor(category.color))

            itemView.setOnClickListener{
                onItemClicked(category)
            }
        }
    }

}