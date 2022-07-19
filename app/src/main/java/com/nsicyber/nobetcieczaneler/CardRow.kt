package com.nsicyber.nobetcieczaneler




import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.transition.Visibility
import com.ace1ofspades.recyclerview.Item
import com.ace1ofspades.recyclerview.ViewHolder
import org.jetbrains.anko.find
import java.text.DecimalFormat
import androidx.core.content.ContextCompat.startActivity




class CardRow(Name:String,Address:String,Phone:String,Distance:String,var location: Location): Item<ViewHolder>() {
    var name=Name
    var address=Address
    var phone=Phone
    var distance=Distance
    override fun bind(p0: ViewHolder, p1: Int) {
        val view=p0.itemView
        var basicLay=view.findViewById<LinearLayout>(R.id.basicInfo)
        var detailLay=view.findViewById<LinearLayout>(R.id.details_info)
        detailLay.visibility=View.GONE
        basicLay.setOnClickListener {
            if(detailLay.isVisible){
                detailLay.visibility=View.GONE
            }
            else
            {
                detailLay.visibility=View.VISIBLE
            }
        }
        view.findViewById<Button>(R.id.locate_button).setOnClickListener {
            val uri = Uri.parse("http://maps.google.com/maps?daddr=${location.latitude},${location.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            p0.root.context.startActivity(intent)
        }


            view.findViewById<Button>(R.id.call_button).setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL);


                intent.data = Uri.parse("tel:${phone.toFormattedPhone()}")

                p0.root.context.startActivity(intent)
            }
            view.findViewById<TextView>(R.id.pharmacy_name).text=name
            view.findViewById<TextView>(R.id.pharmacy_address).text=address
            view.findViewById<TextView>(R.id.pharmacy_phone).text=phone.toFormattedPhone()
            var dist= distance.toDouble().toInt().toKiloMeter()


            view.findViewById<TextView>(R.id.pharmacy_distance).text=dist



    }



    override fun getLayout(): Int {
        return R.layout.row_card


    }


}

internal fun Int.toKiloMeter() :String{
    if(this<1000){
        return this.toString()+" m"
    }
    var number:Double=this.toDouble()/1000
    var df=DecimalFormat("###.#")
    return df.format(number)+" km"
}

internal fun String.toFormattedPhone() :String{
    var text="# (###) ### ## ##"
    var newText:String=""
    var count=0
    for(i in text)
    {
    if(i=='#'){
        newText+=this.get(count)
        count++
    }
        else
    {
        newText+=i
    }
    }
    return newText
}

