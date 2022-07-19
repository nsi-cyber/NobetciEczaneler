package com.nsicyber.nobetcieczaneler

import android.app.ProgressDialog
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager

import android.net.Uri
import android.os.Build

import android.provider.Settings
import android.service.controls.ControlsProviderService.TAG

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.ace1ofspades.recyclerview.GroupAdapting
import com.ace1ofspades.recyclerview.ViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Array
import java.lang.reflect.Type
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {
var comp : Boolean =false
    var progressDialog : ProgressDialog? =null
    var viewPager: ViewPager? =null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var latitudeLabel: String? = null
    private var longitudeLabel: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity,
                    Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_CALL_PHONE)
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    36)
            }
        }


       // LocationPermissionRequest



        latitudeLabel = resources.getString(R.string.latitudeBabel)
        longitudeLabel = resources.getString(R.string.longitudeBabel)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        viewPager = findViewById<ViewPager>(R.id.viewPager)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Data Loading")
        progressDialog!!.setMessage("Application is loading, please wait")
        progressDialog!!.show()







    }


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->


            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                 latitudeLabel = (lastLocation)!!.latitude.toString()
                 longitudeLabel = (lastLocation)!!.longitude.toString()
                getList()
            }
            else {
                Log.w(TAG, "getLastLocation:exception", task.exception)

            }
        }
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    } private fun LocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            36
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    /*
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }}

*/



    fun getList(): ArrayList<PharmacyClass>{

        var call = RetrofitClient.getInstance().create(ApiInterface::class.java).getAll()
        var obj=ArrayList<PharmacyClass>()

        call?.enqueue(object:Callback<JsonObject?>{


            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {


                obj = JSONObject(response.body().toString()).toArrayList<PharmacyClass>()


                viewPager!!.adapter = PageAdapter(supportFragmentManager, obj,lastLocation!!)

                val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
                tabLayout.setupWithViewPager(viewPager)
                progressDialog!!.hide()

            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {

            }


        })
        return obj
    }






    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 35

    }




}

 inline fun <reified T> JSONObject.toArrayList():ArrayList<T> {
    var obj :JSONObject=this
    var list = arrayListOf<T>()
    for(i in obj.keys()){
     var str=obj.getJSONObject(i).toString()
        var obje = fromJson<T>(str)


        if (obje != null) {
            list.add(obje)
        }
    }
    return list
}


/*
 fun <T> fromJson(json: String?): T {
    return Gson().fromJson<T>(json, object: TypeToken<T>(){}.type)
}

*/
inline fun <reified T> fromJson(json: String): T? {
    return try {
        //Get the specific generic type with the help of TypeToken class
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(json, type)
    } catch (e: Exception) {
        null
    }
}