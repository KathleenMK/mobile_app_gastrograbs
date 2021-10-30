package org.wit.gastrograbs.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GrabberModel(var id: Long = 0,
                        var email: String = "",
                        var password: String = "") : Parcelable