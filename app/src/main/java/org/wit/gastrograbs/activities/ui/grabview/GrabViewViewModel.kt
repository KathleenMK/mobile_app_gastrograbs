package org.wit.gastrograbs.activities.ui.grabview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.GrabStore

class GrabViewViewModel : ViewModel() {
    private val grab = MutableLiveData<GrabModel>()

    val observableGrab: LiveData<GrabModel>
    get() = grab

//    fun getGrab(id: Long){
//        grab.value = GrabStore.findOne(id)
//    }



}