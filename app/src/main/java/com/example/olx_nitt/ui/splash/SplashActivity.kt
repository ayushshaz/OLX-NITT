package com.example.olx_nitt.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.olx_nitt.BaseActivity
import com.example.olx_nitt.MainActivity
import com.example.olx_nitt.R
import com.example.olx_nitt.ui.login.LoginActivity
import com.example.olx_nitt.utilities.Constants
import com.example.olx_nitt.utilities.SharedPref
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*

class SplashActivity: BaseActivity() {
    private var MY_PERMISSION_REQUEST_CODE=100
    private var REQUEST_GPS=101
    private var locationRequest:LocationRequest?=null
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)

        getLocationCallback()
    }

    override fun onResume() {
        super.onResume()
        askForPermission()
    }

    private fun askForPermission() {
        val permission= arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this,permission,MY_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
     if(requestCode==MY_PERMISSION_REQUEST_CODE){
         var granted = false
         for(grantResult in grantResults){
             if(grantResult==PackageManager.PERMISSION_GRANTED){
                 granted=true
             }
         }
         if(granted){
             enablegps()
         }
     }

    }

    private fun enablegps() {
        locationRequest=LocationRequest.create()
        locationRequest?.setInterval(3000)
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder= LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest!!)
        val task= LocationServices.getSettingsClient(this).checkLocationSettings(builder.build()) //gives us the location

        task.addOnCompleteListener(object:OnCompleteListener<LocationSettingsResponse>{   // gps is on or not
            override fun onComplete(p0: Task<LocationSettingsResponse>) {
                try {
                    val response=task.getResult(ApiException::class.java)
                    startLocationUpdates()
                }catch(exception:ApiException){
                    when(exception.statusCode){
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{
                            val resolvable= exception as ResolvableApiException  // this code will show the gps request
                            resolvable.startResolutionForResult(this@SplashActivity,REQUEST_GPS)
                        }
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // will check for gps callback
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_GPS){
            startLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
        }


    }
    private fun getLocationCallback() {
        locationCallback=object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                val location=p0?.lastLocation
                //SharedPref(this@SplashActivity).setString(Constants.CITY_NAME,getCityName(location))
               //Toast.makeText(this@SplashActivity,getCityName(location),Toast.LENGTH_SHORT).show()
                if (SharedPref(this@SplashActivity).getString(Constants.USER_ID)?.isEmpty()!!){
                    startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                    finish()
                }
            }
        }
    }
    //Sir it opened
    private fun getCityName(location: Location?): String {
        var cityName=""
        val geocoder= Geocoder(this, Locale.getDefault())
        try {
            val address=geocoder.getFromLocation(location?.latitude!!,location?.longitude!!,1)
            cityName=address[0].locality //but theres something wrong wit getting the location also sir in emulator its working fine because emulator doesnt require location okay sir
        }catch (e:IOException){
            Log.d("locationException","failed")
        }
        return cityName
    }
}