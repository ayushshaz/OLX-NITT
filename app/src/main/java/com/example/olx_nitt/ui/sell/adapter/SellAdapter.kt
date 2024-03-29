package com.example.olx_nitt.ui.sell.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx_nitt.R
import com.example.olx_nitt.model.CategoriesModel
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class SellAdapter(var categoriesList: MutableList<CategoriesModel>,
                  var itemClickListener: ItemClickListener
                        ) : RecyclerView.Adapter<SellAdapter.Viewholder>() {

    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context=parent.context
        val viewholder=LayoutInflater.from(parent.context).inflate(R.layout.adapter_sell,parent,false)
        return Viewholder(viewholder)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.textViewTitle.setText(categoriesList.get(position).key)
        Glide.with(context).load(categoriesList.get(position).image_bw).placeholder(R.drawable.ic_placeholder).into(holder.imageView)
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onItemClick(position)
        })
    }

    fun updateList(temp: MutableList<CategoriesModel>) {
        categoriesList = temp
        notifyDataSetChanged()
    }

    class Viewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val textViewTitle=itemView.findViewById<TextView>(R.id.tvTitle)
        val imageView=itemView.findViewById<ImageView>(R.id.ivIcon)
    }
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}