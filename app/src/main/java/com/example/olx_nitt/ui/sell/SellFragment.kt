package com.example.olx_nitt.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx_nitt.ui.BaseFragment
import com.example.olx_nitt.R
import com.example.olx_nitt.model.CategoriesModel
import com.example.olx_nitt.ui.home.adapter.CategoriesAdapter
import com.example.olx_nitt.ui.sell.adapter.SellAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : BaseFragment(), CategoriesAdapter.ItemClickListener,
    SellAdapter.ItemClickListener {


    val db= FirebaseFirestore.getInstance()
    private lateinit var categoriesModel:MutableList<CategoriesModel>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sell, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCategoryList()
    }
    private fun getCategoryList() {
        showProgressBar()
        db.collection("Categories").get().addOnSuccessListener {
            hideProgressBar()
            categoriesModel = it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
    }

    private fun setAdapter() {
        rvOfferings.layoutManager = GridLayoutManager(context,3)
        val sellAdapter= SellAdapter(categoriesModel,this)
        rvOfferings.adapter = sellAdapter
    }

    override fun onItemClick(position: Int) {
       // Toast.makeText(context, "Its Suppose to move to includeDetailsFragment", Toast.LENGTH_SHORT).show()
        //working now need to just open the fragment to include details  Sir in the xml

        //      childFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,
        //          IncludeDetailFragment()
        //      ).commit()

        var bundle = Bundle() // Not adding bundle before was the reason for crashing ... The data was not getting updated in firebase and Homescreen use to open
        bundle.putString("key",categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_sell_to_include_details,bundle)

    }

}