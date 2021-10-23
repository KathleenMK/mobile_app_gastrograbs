package org.wit.gastrograbs.main

import android.app.Application
import org.wit.gastrograbs.models.GrabJSONStore
import org.wit.gastrograbs.models.GrabMemStore
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.GrabStore
import timber.log.Timber
import timber.log.Timber.i


class MainApp : Application() {

    lateinit var grabs: GrabStore

    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //grabs = GrabMemStore()
        grabs = GrabJSONStore(applicationContext)
        i("GastroGrabs started")
        //grabs.create(GrabModel(0,"Reeses Overload","Chocolatey deliciousness, peanuts and pretzels","treat"))
        //grabs.create(GrabModel(1,"Coffee House Lane Pods","So smooth...","everyday"))
        //grabs.create(GrabModel(2,"Seagull Latte Cruffins","If you like croissants and muffins and flavoured custard...","weekend"))

    }
}