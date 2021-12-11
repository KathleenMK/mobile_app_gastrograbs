package org.wit.gastrograbs.activities.ui.grabadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GrabAddViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the GRAB ADD Fragment"
    }
    val text: LiveData<String> = _text
}