package org.wit.gastrograbs.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface GrabStore {

    fun findAll(grabsList:
                MutableLiveData<List<GrabModel>>
    )
    fun findAll(userid:String,
                grabsList:
                MutableLiveData<List<GrabModel>>
    )
    fun findById(userid:String, grabid: String,
                 grab: MutableLiveData<GrabModel>
    )
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, grab: GrabModel)
    fun delete(userid:String, grabid: String)
    fun update(userid:String, grabid: String, grab: GrabModel)
}