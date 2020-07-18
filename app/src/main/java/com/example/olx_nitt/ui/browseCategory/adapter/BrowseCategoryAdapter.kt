package com.example.olx_nitt.ui.browseCategory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx_nitt.R
import com.example.olx_nitt.model.CategoriesModel
import com.example.olx_nitt.model.DataItemModel
import com.example.olx_nitt.ui.home.adapter.CategoriesAdapter
import com.example.olx_nitt.utilities.Constants
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class BrowseCategoryAdapter(
            var dataItemModel: MutableList<DataItemModel>,
            var mClickListener : ItemClickListener)
        : RecyclerView.Adapter<BrowseCategoryAdapter.Viewholder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val viewholder =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_ads, parent, false)
        return Viewholder(viewholder)
    }

    override fun getItemCount(): Int {
        return dataItemModel.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.textViewPrice.text = Constants.CURRENCY_SYMBOL + dataItemModel.get(position).price
        holder.textViewBrand.text = dataItemModel.get(position).brand
        holder.textViewAddress.text = dataItemModel.get(position).address

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = sdf.format(dataItemModel.get(position).createdDate?.time!!).toString()
        holder.textViewDate.text = formattedDate

        Glide.with(context).load(dataItemModel.get(position).images.get(0))
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)
        holder.itemView.setOnClickListener(View.OnClickListener {
            mClickListener.onItemClick(position)
        })
    }

    fun updateList(temp: MutableList<DataItemModel>) {
        dataItemModel = temp
        notifyDataSetChanged()
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPrice = itemView.findViewById<TextView>(R.id.tvPrice)!!
        val textViewBrand = itemView.findViewById<TextView>(R.id.tvBrand)!!
        val textViewAddress = itemView.findViewById<TextView>(R.id.tvAddress)!!
        val textViewDate = itemView.findViewById<TextView>(R.id.tvDate)!!
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)!!
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

}