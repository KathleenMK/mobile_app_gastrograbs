package org.wit.gastrograbs.main

import android.app.Application
import android.net.Uri
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
        grabs = GrabJSONStore(applicationContext)
        grabbers = GrabberJSONStore(applicationContext)
        i("GastroGrabs started")
        /*
        grabs.create(GrabModel(0,"Reeses Overload","Chocolatey deliciousness, peanuts and pretzels","treat",Uri.EMPTY,
            arrayListOf("This is the most beautiful Reeses product around!", "In Lidl this week...", "Can someone send me some", "Gorgeous"),52.16397003708149,-7.162707075476646,16.0f))
        grabs.create(GrabModel(1,"Coffee House Lane Pods","So smooth...","everyday"))
        grabs.create(GrabModel(2,"Seagull Cruffins","If you like croissants and muffins and flavoured custard...","weekend",Uri.EMPTY,
            arrayListOf("Just gorgeous"),52.16130378544212, -7.151395529508591, 16.0f))
        grabs.create(GrabModel(3,"Valentia Island Vermouth","Really unusual but sweet and great with sparkling wine...","treat"))
        //grabs.create(GrabModel(4,"Cinnamon buns","Another of Seagull's finest","Sweet",Uri.EMPTY,
        //    arrayListOf(),52.16130378544212, -7.151395529508591, 16.0f))
        */
    }
}