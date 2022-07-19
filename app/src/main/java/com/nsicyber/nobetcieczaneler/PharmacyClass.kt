package com.nsicyber.nobetcieczaneler

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class PharmacyClass(
        @SerializedName("ilce")
        val ilce: String?,
        @SerializedName("bolge")
        val bolge: String?,
        @SerializedName("eczane")
        val eczane: String?,
        @SerializedName("tel")
        val tel: String?,
        @SerializedName("adres")
        val adres: String?,
        @SerializedName("adres_tarifi")
        val adres_tarifi: String?,
        @SerializedName("enlem")
        val enlem: String?,
        @SerializedName("boylam")
        val boylam: String?,
        @SerializedName("baslangic")
        val baslangic: String?,
        @SerializedName("bitis")
        val bitis: String?,
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()
                ) {
        }

        override fun writeToParcel(p0: Parcel, flags: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun describeContents(): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        companion object CREATOR : Parcelable.Creator<PharmacyClass> {
                override fun createFromParcel(parcel: Parcel): PharmacyClass {
                        return PharmacyClass(parcel)
                }

                override fun newArray(size: Int): Array<PharmacyClass?> {
                        return arrayOfNulls(size)
                }
        }
}






