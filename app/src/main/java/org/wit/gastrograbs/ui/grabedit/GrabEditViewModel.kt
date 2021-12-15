package org.wit.gastrograbs.ui.grabedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabModel

class GrabEditViewModel : ViewModel() {
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is the GRAB Fragment"
//    }
//    val text: LiveData<String> = _text

    private val grab = MutableLiveData<GrabModel>()

    val observableGrab: LiveData<GrabModel>
        get() = grab


    fun updateGrab(grab: GrabModel){
        GrabManager.update(grab)
    }

    fun removeComment(grab: GrabModel, comment: String) {
        //status.value = try {
        GrabManager.removeComment(grab, comment)
        //     true
        // } catch (e: IllegalArgumentException) {
        //     false
        // }
    }

    fun deleteGrab(grab: GrabModel){
        GrabManager.delete(grab)
    }
}