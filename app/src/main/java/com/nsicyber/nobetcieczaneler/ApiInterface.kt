package com.nsicyber.nobetcieczaneler

import org.json.JSONObject
import retrofit2.Response

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @FormUrlEncoded
    @POST("/nobet-belediye")
    open fun getAll(@Field("item")  item:String?=""): Call<JsonObject?>?
}