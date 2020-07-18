package com.example.olx_nitt.ui

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.olx_nitt.R

open class BaseFragment :Fragment() {
    lateinit var mDialog:Dialog
    open fun showProgressBar() {
        mDialog = Dialog(this.requireActivity()) //activity!! in argument ia giving error need to look at this
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_progressbar)
        mDialog.setCancelable(true)
        mDialog.show()
    }
    open fun hideProgressBar(){
        mDialog.dismiss()
    }
}