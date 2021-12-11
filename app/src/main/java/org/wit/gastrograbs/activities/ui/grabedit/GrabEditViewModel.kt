package org.wit.gastrograbs.activities.ui.grabedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GrabEditViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the GRAB Fragment"
    }
    val text: LiveData<String> = _text
}