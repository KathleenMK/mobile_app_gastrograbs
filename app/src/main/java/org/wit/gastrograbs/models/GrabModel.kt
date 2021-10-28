package org.wit.gastrograbs.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GrabModel(var id: Long = 0,
                     var title: String = "",
                     var description: String = "",
                     var category: String = ""   ,
                     var image: Uri = Uri.EMPTY,
                     var comments: ArrayList<String> = arrayListOf(),
                     var lat : Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
