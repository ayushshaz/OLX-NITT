package com.example.olx_nitt.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx_nitt.R
import com.example.olx_nitt.model.CategoriesModel
import com.example.olx_nitt.ui.home.adapter.CategoriesAdapter
import com.example.olx_nitt.ui.sell.adapter.SellAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : Fragment(), CategoriesAdapter.ItemClickListener,
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
        db.collection("Categories").get().addOnSuccessListener {
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

    }

}