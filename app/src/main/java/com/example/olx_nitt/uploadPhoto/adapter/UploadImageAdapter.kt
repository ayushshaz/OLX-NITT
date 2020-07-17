package com.example.olx_nitt.uploadPhoto.adapter

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx_nitt.R
import com.example.olx_nitt.model.CategoriesModel
import com.example.olx_nitt.ui.home.adapter.CategoriesAdapter
import de.hdodenhof.circleimageview.CircleImageView

class UploadImageAdapter(internal var activity : Activity,
                         internal var imagesArrayList : ArrayList<String>,
                         internal var itemClick : ItemClickListener
                         ) : RecyclerView.Adapter<UploadImageAdapter.Viewholder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context=parent.context
        val viewholder=
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_upload_image,parent,false)
        return Viewholder(viewholder)
    }

    override fun getItemCount(): Int {
        return imagesArrayList.size+1 //bcz icon will be added in last
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        if(position<imagesArrayList.size){
            val bitmap = BitmapFactory.decodeFile(imagesArrayList[position])
            holder.imageView.setImageBitmap(bitmap)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            if(position==imagesArrayList.size){
                itemClick.onItemClick()
            }
        })
    }

    fun updateList(temp: ArrayList<String>) {
        imagesArrayList = temp
        notifyDataSetChanged()
    }

    fun customNotify(selectedImagesArrayList: java.util.ArrayList<String>) {
        this.imagesArrayList = selectedImagesArrayList
        notifyDataSetChanged()
    }

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView=itemView.findViewById<ImageView>(R.id.imageView)
    }
    interface ItemClickListener {
        fun onItemClick()
    }
}