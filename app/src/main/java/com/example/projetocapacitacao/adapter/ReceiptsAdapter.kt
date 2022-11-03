package com.example.projetocapacitacao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projetocapacitacao.R
import com.example.projetocapacitacao.helper.StringHelper
import com.example.projetocapacitacao.model.ReceiptXX
import java.text.NumberFormat
import java.time.format.DateTimeFormatter

class ReceiptsAdapter(private val onItemClicked: (ReceiptXX) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _list: List<ReceiptXX> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReceiptsViewHolder(LayoutInflater.from(parent.context).
        inflate(R.layout.itemview_receipts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ReceiptsViewHolder ->{
                holder.bind(_list.get(position), onItemClicked)
            }
        }
    }

    override fun getItemCount(): Int {
        return _list.size
    }

    fun setDataset(list: List<ReceiptXX>){
        _list = list
        notifyDataSetChanged()
    }


    class ReceiptsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val image = itemView.findViewById<ImageView>(R.id.imageView_Receipts)
        private val title = itemView.findViewById<TextView>(R.id.textLTitle_Receipts)
        private val description = itemView.findViewById<TextView>(R.id.textDescription_Receipts)
        private val value = itemView.findViewById<TextView>(R.id.textValor_Receipts)

        fun bind(receipt: ReceiptXX, onItemClicked: (ReceiptXX) -> Unit){
            Glide.with(itemView.context).load(receipt.merchantIcon).into(image)
            title.text = receipt.merchantName
            description.text = StringHelper.getFormattedDate(receipt.date)
            value.text = StringHelper.getFormattedCurrency(receipt.value)

            itemView.setOnClickListener{
                onItemClicked(receipt)
            }
        }
    }

}