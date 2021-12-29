package org.wit.gastrograbs.ui.grabcollection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import org.wit.gastrograbs.firebase.FirebaseDBManager
//import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber
import java.lang.Exception

class GrabCollectionViewModel : ViewModel() {

    private val grabs = MutableLiveData<List<GrabModel>>()

    val observableGrabsList: LiveData<List<GrabModel>>
        get() = grabs

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        loadAll()
    }

    fun load() {
        try {
               FirebaseDBManager.findAll(
                liveFirebaseUser.value?.uid!!,
                grabs
            )
        }
        catch (e: Exception) {
            Timber.i("In GrabCollectionViewModel LOAD : $e.message")
        }
    }

    fun loadAll() {
        try {
            FirebaseDBManager.findAll(grabs)
            Timber.i("Grab Collection LoadAll Success : ${grabs.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Grab Collection LoadAll Error : $e.message")
        }
    }
}