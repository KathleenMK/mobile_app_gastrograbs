package org.wit.gastrograbs.ui.grabview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabModel

class GrabViewViewModel : ViewModel() {
    private val grab = MutableLiveData<GrabModel>()

    val observableGrab: LiveData<GrabModel>
    get() = grab

    fun getGrab(grabspecific: GrabModel){
        grab.value = GrabManager.findOne(grabspecific.id)
    }

    fun addComment(grab: GrabModel, comment: String) {
        //status.value = try {
            GrabManager.addComment(grab,comment)
       //     true
       // } catch (e: IllegalArgumentException) {
       //     false
       // }
    }



}