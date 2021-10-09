package org.wit.gastrograbs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import timber.log.Timber
import timber.log.Timber.i

class GrabActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grab)
        Timber.plant(Timber.DebugTree())
        i("Grab Activity started..")
     }
}