package org.wit.gastrograbs.main

import android.app.Application
import org.wit.gastrograbs.models.GrabMemStore
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber
import timber.log.Timber.i


class MainApp : Application() {

    val grabs = GrabMemStore()

    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("GastroGrabs started")
        grabs.create(GrabModel(0,"Reeses Overload","Chocolatey deliciousness, peanuts and pretzels","treat"))
        grabs.create(GrabModel(1,"Coffee House Lane Pods","So smooth...","everyday"))
        grabs.create(GrabModel(2,"Seagull Latte Cruffins","If you like croissants and muffins and flavoured custard...","weekend"))

    }
}