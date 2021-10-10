package org.wit.gastrograbs.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
//import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivityGrabBinding
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber
import timber.log.Timber.i



class GrabActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrabBinding
    var grab = GrabModel()  //creating grab as a class member

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_grab)
        Timber.plant(Timber.DebugTree())
        i("Grab Activity started..")

        binding = ActivityGrabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener() {
            grab.title = binding.grabTitle.text.toString()
            if (grab.title.isNotEmpty()) {
                i("add Button Pressed: $grab.title")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
     }
}