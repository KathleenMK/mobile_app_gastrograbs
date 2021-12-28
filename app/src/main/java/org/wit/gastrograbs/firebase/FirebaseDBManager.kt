package org.wit.gastrograbs.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.GrabStore
import timber.log.Timber

object FirebaseDBManager : GrabStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance("https://gastrograbs-app-default-rtdb.europe-west1.firebasedatabase.app").reference
            // above from https://stackoverflow.com/questions/68196128/firebase-wants-me-to-change-my-database-url-due-to-region 16Dec21
    override fun findAll(grabsList: MutableLiveData<List<GrabModel>>) {
                database.child("grabs")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            Timber.i("Firebase error : ${error.message}")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val localList = ArrayList<GrabModel>()
                            val children = snapshot.children
                            children.forEach {
                                val grab = it.getValue(GrabModel::class.java)
                                localList.add(grab!!)
                            }
                            database.child("grabs")
                                .removeEventListener(this)

                            grabsList.value = localList
                        }
                    })
    }

    override fun findAll(userid: String, grabsList: MutableLiveData<List<GrabModel>>) {

        database.child("user-grabs").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Adding Grab error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<GrabModel>()
                    val children = snapshot.children
                    children.forEach {
                        val grab = it.getValue(GrabModel::class.java)
                        localList.add(grab!!)
                    }
                    database.child("user-grabs").child(userid)
                        .removeEventListener(this)

                    grabsList.value = localList
                }
            })
    }

    override fun findById(userid: String, grabid: String, grab: MutableLiveData<GrabModel>) {

        database.child("user-grabs").child(userid)
            .child(grabid).get().addOnSuccessListener {
                grab.value = it.getValue(GrabModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, grab: GrabModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("grabs").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        grab.uid = key
        val grabValues = grab.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/grabs/$key"] = grabValues
        childAdd["/user-grabs/$uid/$key"] = grabValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, grabid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/grabs/$grabid"] = null
        childDelete["/user-grabs/$userid/$grabid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, grabid: String, grab: GrabModel) {

        val grabValues = grab.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["grabs/$grabid"] = grabValues
        childUpdate["user-grabs/$userid/$grabid"] = grabValues

        database.updateChildren(childUpdate)
    }

    fun updateImage(userid: String, grabid: String, grab: GrabModel, imageUri: String) {

        val grabValues = grab.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["grabs/$grabid"] = grabValues
        childUpdate["user-grabs/$userid/$grabid"] = grabValues

        database.updateChildren(childUpdate)
    }
}