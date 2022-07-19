package com.nsicyber.nobetcieczaneler

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ace1ofspades.recyclerview.GroupAdapting
import com.ace1ofspades.recyclerview.ViewHolder
import kotlin.math.hypot
import kotlin.math.sqrt

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ListFragment : Fragment() {
    var adapter= GroupAdapting<ViewHolder>()
    var list=arrayListOf<PharmacyClass>()
    var curLoc: Location? =null
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val args = arguments



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_list, container, false)
        // Inflate the layout for this fragment
        var recycleView=view.findViewById<RecyclerView>(R.id.recyclerView)
        var name:String=""
        var phone:String=""
        var address:String=""


        recycleView.layoutManager= GridLayoutManager(context,1)
        recycleView.adapter=adapter
        list.sortBy {
            var endPoint : Location= Location("end")
            endPoint.latitude= it.enlem!!.toFloat().toDouble()
            endPoint.longitude= it.boylam!!.toFloat().toDouble()
            var distance= curLoc?.distanceTo(endPoint)
           distance
        }
        for(x in list){

            var endPoint : Location= Location("end")
            endPoint.latitude= x.enlem!!.toFloat().toDouble()
                endPoint.longitude= x.boylam!!.toFloat().toDouble()
            var distance= curLoc?.distanceTo(endPoint)
            //var distance=(sqrt((((curLocX-x.enlem.toString().toLong())*(curLocX-x.enlem.toString().toLong()))+((curLocY-x.boylam.toString().toLong())*(curLocY-x.boylam.toString().toLong()))).toFloat() ) )


        adapter.add(CardRow(x.eczane.toString(),x.adres.toString(),x.tel.toString(),distance.toString(),endPoint))



        }
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}