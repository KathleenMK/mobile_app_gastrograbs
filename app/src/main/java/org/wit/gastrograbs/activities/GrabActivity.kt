package org.wit.gastrograbs.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
//import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivityGrabBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber
import timber.log.Timber.i



class GrabActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrabBinding
    var grab = GrabModel()  //creating grab as a class member
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_grab)
//        Timber.plant(Timber.DebugTree())

        app = application as MainApp
        i("Grab Activity started..")

        binding.btnAdd.setOnClickListener() {
            grab.title = binding.grabTitle.text.toString()
            grab.description = binding.grabDescription.text.toString()
            grab.category = binding.grabCategory.text.toString()
            if (grab.title.isNotEmpty()) {
                app.grabs.add(grab.copy())
                i("add Button Pressed: $grab")
                for (i in app.grabs.indices)
                {i("Grab[$i]:${this.app.grabs[i]}")}
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
     }
}