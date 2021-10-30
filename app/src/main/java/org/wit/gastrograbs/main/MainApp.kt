package org.wit.gastrograbs.main

import android.app.Application
import android.net.Uri
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
        /*
        grabs.create(GrabModel(0,"Reeses Overload","Chocolatey deliciousness, peanuts and pretzels","treat",Uri.EMPTY,
            arrayListOf("This is the most beautiful Reeses product around!", "In Lidl this week...", "Can someone send me some", "Gorgeous"),52.16397003708149,-7.162707075476646,16.0f))
        grabs.create(GrabModel(1,"Coffee House Lane Pods","So smooth...","everyday"))
        grabs.create(GrabModel(2,"Seagull Cruffins","If you like croissants and muffins and flavoured custard...","weekend",Uri.EMPTY,
            arrayListOf("Just gorgeous"),52.16130378544212, -7.151395529508591, 16.0f))
        */
    }
}