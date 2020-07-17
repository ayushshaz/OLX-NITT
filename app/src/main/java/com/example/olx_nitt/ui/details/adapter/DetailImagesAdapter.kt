package com.example.olx_nitt.ui.details.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.olx_nitt.R

//ViewPager Adapter

class DetailImagesAdapter(var context : Context,
                          private val imagelist : ArrayList<String>,
                          var onItemClickListener : OnItemClick
                          ) : PagerAdapter(){

    private var inflater: LayoutInflater? = null
    private var doNotifyDataSetChangedOnce = false

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ==`object`
    }

    override fun getCount(): Int {
        if(doNotifyDataSetChangedOnce){
            doNotifyDataSetChangedOnce = false
            notifyDataSetChanged()
        }
        return imagelist.size
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater?.inflate(R.layout.adapter_images_details,container,false)
        val imageView = view?.findViewById<ImageView>(R.id.imageView)

        imageView?.setOnClickListener(View.OnClickListener {
            onItemClickListener.onClick(position)
        })

        Glide.with(context).load(imagelist.get(position))
            .placeholder(R.drawable.big_placeholder)
            .into(imageView!!)
        container.addView(view,0)
        return view
    }
    interface OnItemClick{
        fun onClick(position : Int)
    }
}