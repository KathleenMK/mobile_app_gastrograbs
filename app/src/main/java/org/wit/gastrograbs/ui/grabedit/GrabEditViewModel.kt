package org.wit.gastrograbs.ui.grabedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.gastrograbs.firebase.FirebaseDBManager
//import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber

class GrabEditViewModel : ViewModel() {

    private val grab = MutableLiveData<GrabModel>()

    var observableGrab: LiveData<GrabModel>
        get() = grab
        set(value) {
            grab.value = value.value
        }


    fun updateGrab(userid: String, id: String, grab: GrabModel) {
        //GrabManager.update(grab)
        Timber.i("in updateGrab GrabEditViewMOdel")
        FirebaseDBManager.update(userid, id, grab)
    }

    fun updateImage(userid: String, id: String, grab: GrabModel, imageUri: String) {
        //GrabManager.update(grab)
        Timber.i("in updateGrab GrabEditViewMOdel")
        FirebaseDBManager.updateImage(userid, id, grab, imageUri)
    }

    fun deleteGrab(userid: String, id: String) {                          //(grab: GrabModel){
        //GrabManager.delete(grab)
        FirebaseDBManager.delete(userid, id)
    }
}