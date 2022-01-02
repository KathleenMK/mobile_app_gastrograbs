package org.wit.gastrograbs.ui.grabview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.gastrograbs.firebase.FirebaseDBManager
//import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber

class GrabViewViewModel : ViewModel() {
    private val grab = MutableLiveData<GrabModel>()

    var observableGrab: LiveData<GrabModel>
        get() = grab
        set(value) {
            grab.value = value.value
        }

    fun getGrab(id: String) {
        FirebaseDBManager.findById(id, grab)
    }

    fun updateGrab(userid: String, id: String, grab: GrabModel) {
        Timber.i("in updateGrab GrabViewViewMOdel")
        FirebaseDBManager.update(userid, id, grab)
    }

}