package com.example.olx_nitt.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.olx_nitt.R
import com.example.olx_nitt.ui.BaseFragment
import com.example.olx_nitt.utilities.Constants
import com.example.olx_nitt.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_setings.*

class SettingsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_setings,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edName.setText(SharedPref(requireActivity()).getString(Constants.USER_NAME))
        edEmailAddress.setText(SharedPref(requireActivity()).getString(Constants.USER_EMAIL))
        edPhone.setText(SharedPref(requireActivity()).getString(Constants.USER_PHONE))
        edPostalAddress.setText(SharedPref(requireActivity()).getString(Constants.USER_ADDRESS))

        listener()
    }

    private fun listener() {
        tvSave.setOnClickListener(View.OnClickListener {
            saveData()
        })
    }

    private fun saveData() {
        if(edName.text.toString().isEmpty())
            edName.setError(getString(R.string.enter_name))
        else if(edEmailAddress.text.toString().isEmpty())
            edEmailAddress.setError(getString(R.string.enter_email_address))
        else if(edPhone.text.toString().isEmpty())
            edPhone.setError(getString(R.string.enter_phone_number))
        else {
            SharedPref(requireActivity()).setString(Constants.USER_NAME,edName.text.toString())
            SharedPref(requireActivity()).setString(Constants.USER_EMAIL,edEmailAddress.text.toString())
            SharedPref(requireActivity()).setString(Constants.USER_PHONE,edPhone.text.toString())
            SharedPref(requireActivity()).setString(Constants.USER_ADDRESS,edPostalAddress.text.toString())

           Toast.makeText(context,getString(R.string.saved_success),Toast.LENGTH_SHORT).show()
//            fragmentManager?.popBackStack()  // will try replacing with childFragmentManager
            findNavController().navigate(R.id.action_settings_to_profile)
        }
    }
}