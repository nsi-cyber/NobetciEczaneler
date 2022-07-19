package com.nsicyber.nobetcieczaneler

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


import com.yandex.mapkit.map.PlacemarkMapObject

import com.yandex.mapkit.map.PolylineMapObject

import com.yandex.mapkit.map.PolygonMapObject

import com.yandex.runtime.image.AnimatedImageProvider
import com.yandex.mapkit.map.IconStyle

import com.yandex.runtime.ui_view.ViewProvider

import android.widget.TextView

import com.yandex.mapkit.map.CircleMapObject

import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.geometry.*

import com.yandex.mapkit.map.MapObject

import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.Runtime.getApplicationContext
import java.util.*
import kotlin.collections.ArrayList
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import org.jetbrains.anko.toast


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
//9fc569b8-cf4b-4143-8b61-ac5f339bff01


class MapFragment : Fragment() {

    var list=arrayListOf<PharmacyClass>()
    lateinit var curLoc: Location
    lateinit var card:CardView





    private var param1: String? = null
    private var param2: String? = null

    private var mapView: MapView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("c4ce0899-f833-46d6-8645-13dce892bf08")
        MapKitFactory.initialize(context)
        DirectionsFactory.initialize(context)
        list.sortBy {
            var endPoint : Location= Location("end")
            endPoint.latitude= it.enlem!!.toFloat().toDouble()
            endPoint.longitude= it.boylam!!.toFloat().toDouble()
            var distance= curLoc?.distanceTo(endPoint)
            distance
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapview)
        card=view.findViewById(R.id.pharmacy_cardd)
        card.visibility=View.GONE
        for(i in 0..10){
            PharmacyLocationMark(Point(list.get(i).enlem!!.toDouble(),list.get(i).boylam!!.toDouble()), mapView!!,i,view)
        }
        MyLocationMark(Point(curLoc.latitude,curLoc.longitude),mapView!!)

        mapView!!.map.move(
            CameraPosition(Point(curLoc.latitude,curLoc.longitude), 15.0f, 0.0f, 0.0f)
        )
        mapView!!.setOnTouchListener { view, motionEvent ->
            card.visibility=View.GONE

            true }

    }



    private fun PharmacyLocationMark(point: Point, mapview: MapView,index: Int,views: View) {
        val view = View(context).apply {
            background = getDrawable(context,R.drawable.icon_point)
        }

        mapview.map.mapObjects.addPlacemark(point, ViewProvider(view)).addTapListener{map0bject,point->
            Log.d(TAG,"onMapObjectTap:${point ?. latitude}${point ?. longitude}")
            card.visibility=View.VISIBLE

            views.findViewById<CardView>(R.id.pharmacy_cardd).
            findViewById<Button>(R.id.locate_buttonn).
            setOnClickListener {
                val uri = Uri.parse("http://maps.google.com/maps?daddr=${list.get(index).enlem},${list.get(index).boylam}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
               context?.startActivity(intent)
            }


            views.findViewById<Button>(R.id.call_buttonn).setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL);


                intent.data = Uri.parse("tel:${list.get(index).tel?.toFormattedPhone()}")

               context?.startActivity(intent)
            }
            views.findViewById<TextView>(R.id.pharmacy_name).text=list.get(index).eczane
            views.findViewById<TextView>(R.id.pharmacy_address).text=list.get(index).adres
            views.findViewById<TextView>(R.id.pharmacy_phone).text=list.get(index).tel?.toFormattedPhone()
            var endPoint : Location= Location("end")
            endPoint.latitude= list.get(index).enlem!!.toFloat().toDouble()
            endPoint.longitude= list.get(index).boylam!!.toFloat().toDouble()
            var distance= curLoc?.distanceTo(endPoint)

            var dist= distance.toDouble().toInt().toKiloMeter()


            views.findViewById<TextView>(R.id.pharmacy_distance).text=dist





            activity?.toast("Here")
            true
    }}





    private fun MyLocationMark(point: Point, mapview: MapView) {
        val view = View(context).apply {
            background = getDrawable(context,R.drawable.icon_location)
        }

        mapview.map.mapObjects.addPlacemark(point, ViewProvider(view))
    }

























}