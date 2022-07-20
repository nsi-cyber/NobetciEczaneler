package com.nsicyber.nobetcieczaneler

import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


import com.yandex.runtime.ui_view.ViewProvider

import android.widget.TextView

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import com.yandex.mapkit.geometry.*
import com.yandex.mapkit.map.PlacemarkMapObject

import java.util.*


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

    lateinit var iconView: ViewProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("c4ce0899-f833-46d6-8645-13dce892bf08")
        MapKitFactory.initialize(context)
        list.sortBy {
            var endPoint : Location= Location("end")
            endPoint.latitude= it.enlem!!.toFloat().toDouble()
            endPoint.longitude= it.boylam!!.toFloat().toDouble()
            var distance= curLoc?.distanceTo(endPoint)
            distance
        }
        iconView = ViewProvider(View(context).apply {
            background = getDrawable(context,R.drawable.icon_point)
        })

    }

    fun mapObjectRes(mapview: MapView){
        //mapview.map.mapObjects.clear()
        for(i in 0..10){
            var item = list.get(i)
            item.placemark?.let { mapview.map.mapObjects.remove(it) }

            var point = Point((item).enlem!!.toDouble(),(item).boylam!!.toDouble())
            item.placemark = mapview.map.mapObjects.addPlacemark(point, iconView)
            item.placemark?.addTapListener { mapObject, point ->
                PharmacyLocationMark(item, mapview)
                true
            }
        }
        MyLocationMark(Point(curLoc.latitude,curLoc.longitude),mapView!!)

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
        mapView!!.map.move(
            CameraPosition(Point(curLoc.latitude,curLoc.longitude), 15.0f, 0.0f, 0.0f)
        )
        mapView!!.map.addCameraListener { map, cameraPosition, cameraUpdateReason, b ->
            print(cameraUpdateReason)

            mapObjectRes(mapView!!)
        }


    }

    fun testHandler(handler: () -> Unit){
        handler()
    }


    private fun PharmacyLocationMark(item: PharmacyClass, mapview: MapView) {
        Log.d(TAG,"onMapObjectTap:${item.placemark?.geometry?.latitude}${item.placemark?.geometry?.longitude}")


        card.visibility=View.VISIBLE
        card.setOnClickListener {
            card.visibility=View.GONE
            view?.let { it1 -> mapObjectRes(mapView!!) }
        }



        view?.findViewById<Button>(R.id.locate_buttonn)?.
        setOnClickListener {
            val uri = Uri.parse("http://maps.google.com/maps?daddr=${item.enlem},${item.boylam}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(intent)
        }

        view?.findViewById<Button>(R.id.call_buttonn)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL);


            intent.data = Uri.parse("tel:${item.tel?.toFormattedPhone()}")

            context?.startActivity(intent)
        }

        view?.findViewById<TextView>(R.id.pharmacy_name)?.text=item.eczane
        view?.findViewById<TextView>(R.id.pharmacy_address)?.text=item.adres
        view?.findViewById<TextView>(R.id.pharmacy_phone)?.text=item.tel?.toFormattedPhone()


        var endPoint : Location= Location("end")
        endPoint.latitude= item.enlem!!.toFloat().toDouble()
        endPoint.longitude= item.boylam!!.toFloat().toDouble()
        var distance= curLoc?.distanceTo(endPoint)

        var dist= distance.toDouble().toInt().toKiloMeter()


        view?.findViewById<TextView>(R.id.pharmacy_distance)?.text=dist

        mapObjectRes(mapview)
    }





    private fun MyLocationMark(point: Point, mapview: MapView) {
        val view = View(context).apply {
            background = getDrawable(context,R.drawable.icon_location)
        }

        mapview.map.mapObjects.addPlacemark(point, ViewProvider(view))

    }

























}