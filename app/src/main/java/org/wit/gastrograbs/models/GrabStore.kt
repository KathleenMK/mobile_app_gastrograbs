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

//    fun findAll(): List<GrabModel>
//    fun findOne(id: Long): GrabModel?
//    fun create(grab: GrabModel)
//    fun update(grab: GrabModel)
//    fun addComment(grab: GrabModel, comment: String)
//    fun removeComment(grab: GrabModel, comment:String)
//    fun delete(grab: GrabModel)
}