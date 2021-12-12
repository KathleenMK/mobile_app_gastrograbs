package org.wit.gastrograbs.activities.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.gastrograbs.models.GrabJSONStore
import org.wit.gastrograbs.models.GrabManager
import org.wit.gastrograbs.models.GrabStore
import org.wit.gastrograbs.models.GrabModel

class GrabCollectionViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    private val grabs = MutableLiveData<List<GrabModel>>()

    val observableGrabsList: LiveData<List<GrabModel>>
        get() = grabs

    init {
        load()
    }

    fun load() {
        grabs.value = GrabManager.findAll()
    }
}