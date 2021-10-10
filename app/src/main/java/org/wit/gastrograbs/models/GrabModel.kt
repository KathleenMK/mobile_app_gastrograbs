package org.wit.gastrograbs.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GrabModel(var id: Long = 0,
                     var title: String = "",
                     var description: String = "",
                     var category: String = ""   ) : Parcelable