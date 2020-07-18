package com.example.olx_nitt.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.olx_nitt.R
import com.example.olx_nitt.ui.BaseFragment
import com.example.olx_nitt.ui.login.LoginActivity
import com.example.olx_nitt.utilities.Constants
import com.example.olx_nitt.utilities.SharedPref
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), View.OnClickListener {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        listener()
    }

    private fun listener() {
        ll_settings.setOnClickListener(this)
        ll_logout.setOnClickListener(this)
    }

    private fun setData() {
        tvName.text = SharedPref(requireActivity()).getString(Constants.USER_NAME)
        tvEmail.text = SharedPref(requireActivity()).getString(Constants.USER_EMAIL)
        Glide.with(requireActivity())
            .load(SharedPref(requireActivity()).getString(Constants.USER_PHOTO))
            .placeholder(R.drawable.avatar)
            .into(imageViewUser)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
                R.id.ll_settings->{

                }
                R.id.ll_logout->{
                    showAlertDialogBox()
                }
        }
    }

    private fun showAlertDialogBox() {
        var builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.logout))
            builder.setMessage(getString(R.string.sure_logout))
            builder.setIcon(R.drawable.ic_warning)
            builder.setPositiveButton(getString(R.string.yes)){
                dialogInterface: DialogInterface?, i: Int ->
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                clearSession()
                startActivity(Intent(requireActivity(),LoginActivity::class.java))
                requireActivity().finish()
                dialogInterface?.dismiss()
            }

        builder.setNegativeButton(getString(R.string.no)){
            dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val alertDialog:AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun clearSession() {
        SharedPref(requireActivity()).setString(Constants.USER_NAME,"")
        SharedPref(requireActivity()).setString(Constants.USER_PHOTO,"")
        SharedPref(requireActivity()).setString(Constants.USER_EMAIL,"")
        SharedPref(requireActivity()).setString(Constants.USER_ID,"")
        SharedPref(requireActivity()).setString(Constants.USER_PHONE,"")
    }
}