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
        grabs.add(GrabModel("Reeses Overload","Chocolatey deliciousness, peanuts and pretzels","treat"))
        grabs.add(GrabModel("Coffee House Lane Pods","So smooth...","everyday"))
        grabs.add(GrabModel("Seagull Latte Cruffins","If you like croissants and muffins and flavoured custard...","weekend"))
    }
}