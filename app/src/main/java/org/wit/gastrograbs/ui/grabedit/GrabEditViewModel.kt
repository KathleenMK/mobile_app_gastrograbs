package org.wit.gastrograbs.ui.grabedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.gastrograbs.firebase.FirebaseDBManager
//import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber

class GrabEditViewModel : ViewModel() {
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is the GRAB Fragment"
//    }
//    val text: LiveData<String> = _text

    private val grab = MutableLiveData<GrabModel>()

    var observableGrab: LiveData<GrabModel>
        get() = grab
        set(value) {grab.value = value.value}


    fun updateGrab(userid:String, id: String, grab: GrabModel){
        //GrabManager.update(grab)
        Timber.i("in updateGrab GrabEditViewMOdel")
        FirebaseDBManager.update(userid, id, grab)
    }

    fun removeComment(grab: GrabModel, comment: String) {
        //status.value = try {
       //****************** GrabManager.removeComment(grab, comment)
        //     true
        // } catch (e: IllegalArgumentException) {
        //     false
        // }
    }

    fun deleteGrab(userid: String, id: String) {                          //(grab: GrabModel){
        //GrabManager.delete(grab)
        FirebaseDBManager.delete(userid,id)
    }
}