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
                     var image: Uri = Uri.EMPTY,
                     var comments: ArrayList<String> = arrayListOf(),
                     var lat : Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f,
                     var email: String? = "test@test.com") : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "category" to category,
            "email" to email
        )
    }
}

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

