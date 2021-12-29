package org.wit.gastrograbs.ui.grabadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import org.wit.gastrograbs.firebase.FirebaseDBManager
//import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabModel

class GrabAddViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addGrab(firebaseUser: MutableLiveData<FirebaseUser>, grab: GrabModel) {
        status.value = try {
            FirebaseDBManager.create(firebaseUser,grab)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}