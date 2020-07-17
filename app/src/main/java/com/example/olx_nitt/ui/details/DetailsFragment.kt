package com.example.olx_nitt.ui.details

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.olx_nitt.BaseFragment
import com.example.olx_nitt.R
import com.example.olx_nitt.model.DataItemModel
import com.example.olx_nitt.ui.PreviewImageActivity
import com.example.olx_nitt.ui.details.adapter.DetailImagesAdapter
import com.example.olx_nitt.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : BaseFragment(), DetailImagesAdapter.OnItemClick {
    private lateinit var dataItemModel: DataItemModel
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_details,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getItemDetails()
        clickListener()

//        if(arguments?.getString(Constants.KEY).equals(Constants.BOOKS)){
//            llkmDriven.visibility = View.VISIBLE
//        }
    }

    private fun clickListener() {
        tvCall.setOnClickListener(View.OnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:"+dataItemModel.phone)
            startActivity(dialIntent)
        })
    }

    private fun getItemDetails() {
        showProgressBar()
        db.collection(arguments?.getString(Constants.KEY)!!)
            .document(arguments?.getString(Constants.DOCUMENT_ID)!!).get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel = it.toObject(DataItemModel::class.java)!!
                setData()
                setPagerAdapter()
            }
    }

    private fun setPagerAdapter() {
        val detailImagesAdapter = DetailImagesAdapter(requireContext(),dataItemModel.images,this)
        viewPager.adapter = detailImagesAdapter
        viewPager.offscreenPageLimit = 1
        //ViewPager is set
    }

    private fun setData() {
        tvPrice.text = Constants.CURRENCY_SYMBOL +dataItemModel.price
        tvTitle.text = dataItemModel.adTitle
        tvAddress.text = dataItemModel.address
        tvBrand.text = dataItemModel.brand
        tvDescription.text = dataItemModel.description
        tvKmDriven.text = dataItemModel.kmdriven
        tvPhone.text = dataItemModel.phone
        tvYear.text = dataItemModel.year
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        tvDate.text = dateFormat.format(dataItemModel.createdDate)

    }

    override fun onClick(position: Int) {
        startActivity(Intent(activity,PreviewImageActivity::class.java).putExtra("imageUrl",dataItemModel.images.get(position)))

    }
}