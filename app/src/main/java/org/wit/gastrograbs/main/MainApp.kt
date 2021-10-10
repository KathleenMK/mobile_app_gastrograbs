package org.wit.gastrograbs.main

import android.app.Application
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber
import timber.log.Timber.i


class MainApp : Application() {

    val grabs = ArrayList<GrabModel>()

    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("GastroGrabs started")
    }
}