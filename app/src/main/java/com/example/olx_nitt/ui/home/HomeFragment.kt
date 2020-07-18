package com.example.olx_nitt.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olx_nitt.ui.BaseFragment
import com.example.olx_nitt.R
import com.example.olx_nitt.model.CategoriesModel
import com.example.olx_nitt.ui.home.adapter.CategoriesAdapter
import com.example.olx_nitt.utilities.Constants
import com.example.olx_nitt.utilities.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), CategoriesAdapter.ItemClickListener {
    private var categoriesAdapter: CategoriesAdapter?=null
    val db=FirebaseFirestore.getInstance()
    private var categoriesModel:MutableList<CategoriesModel> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvCityName.text=SharedPref(requireActivity()).getString(Constants.CITY_NAME)
        getCategoryList()
        textListener()
    }
    //Next two function for Search
    private fun textListener() {

        edSearch.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                filterList(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    private fun filterList(s: String) {
        var temp:MutableList<CategoriesModel> = ArrayList()
        for(data in categoriesModel){
            if(data.key.contains(s.capitalize())||data.key.contains(s)){
                temp.add(data)
            }
        }
        categoriesAdapter?.updateList(temp)
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
        rv_categories.layoutManager = GridLayoutManager(context,3)
        categoriesAdapter = CategoriesAdapter(categoriesModel,this)
        rv_categories.adapter = categoriesAdapter
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putString(Constants.KEY,categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_home_to_browse,bundle)
    }
}