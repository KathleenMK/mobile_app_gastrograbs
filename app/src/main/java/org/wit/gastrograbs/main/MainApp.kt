package org.wit.gastrograbs.main

import android.app.Application
import android.net.Uri
import org.wit.gastrograbs.firebase.FirebaseDBManager
import org.wit.gastrograbs.models.*
import timber.log.Timber
import timber.log.Timber.i


class MainApp : Application() {

    lateinit var grabs: GrabStore
    lateinit var grabbers: GrabberStore

    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //grabs = GrabMemStore()
        //grabs = GrabJSONStore(applicationContext)
        //grabbers = GrabberJSONStore(applicationContext)
        i("GastroGrabs started")





    }
}