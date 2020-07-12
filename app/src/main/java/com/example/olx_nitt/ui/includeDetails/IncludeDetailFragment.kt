package com.example.olx_nitt.ui.includeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.olx_nitt.BaseFragment
import com.example.olx_nitt.R
import com.example.olx_nitt.utilities.Constants.BOOKS
import com.facebook.appevents.internal.Constants
import kotlinx.android.synthetic.main.fragment_include_details.*

class IncludeDetailFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_include_details,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listener()
        //check for car
//        if(arguments?.getString(com.example.olx_nitt.utilities.Constants.KEY)!!.equals(com.example.olx_nitt.utilities.Constants.BOOKS)){
//            llKmDriven.visibility=View.VISIBLE
//        }
    }

    private fun listener() {
        textViewNext.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.textViewNext->{
                sendData()
            }
        }
    }

    private fun sendData() {
        if(edBrand.text?.isEmpty()!!){
            edBrand.setError(getString(R.string.enter_brand_name))
        }
        else if(edPhone.text?.isEmpty()!!){
            edBrand.setError(getString(R.string.enter_phone_number))
        }
        else{
            val bundle = Bundle()
            bundle.putString(com.example.olx_nitt.utilities.Constants.BRAND,edBrand.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.YEAR,edYear.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.AD_TITLE,edAdTitle.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.AD_DESCRIPTION,edDescription.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.ADDRESS,edAddress.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.PHONE,edPhone.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.PRICE,edPrice.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.KM_DRIVEN,edKmDriven.text.toString())
            bundle.putString(com.example.olx_nitt.utilities.Constants.KEY,arguments?.getString(com.example.olx_nitt.utilities.Constants.KEY))
            findNavController().navigate(R.id.action_details_photo_upload,bundle)
        }
    }
}