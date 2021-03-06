package org.wit.gastrograbs.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class GrabModel(var uid: String? = "",
                     var title: String = "",
                     var description: String = "",
                     var category: String = ""   ,
                     var image: String = ""   , //Uri = Uri.EMPTY,
                     var comments: ArrayList<String> = arrayListOf(),
                     var lat : Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f,
                     var email: String = "test@test.com",
                     var userid: String = "",
                     var ratings: ArrayList<Double> = arrayListOf(),
                     var avrating: String = ""  ) : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "category" to category,
            "email" to email,
            "comments" to comments,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "image" to image,    //seems to need a string value
            "userid" to userid,
            "ratings" to ratings,
            "avrating" to avrating
        )
    }
}

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

