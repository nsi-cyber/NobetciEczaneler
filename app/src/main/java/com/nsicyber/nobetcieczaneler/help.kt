package com.nsicyber.nobetcieczaneler

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class help {
    fun <T> JSONObject.toArrayList():ArrayList<T> {
        var obj : JSONObject =this
        var list = arrayListOf<T>()
        for(i in obj.keys()){
            var obje= obj.getJSONObject(i).fromJson<T>()

            list.add(obje)
        }
        return list
    }
    fun <T> JSONObject.fromJson():T {
        var item= Gson().fromJson<T>(this.toString(),object :
            TypeToken<T>(){}.type)
        return item
    }


}

