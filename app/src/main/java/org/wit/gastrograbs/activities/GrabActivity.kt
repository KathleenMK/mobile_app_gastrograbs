package org.wit.gastrograbs.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
//import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivityGrabBinding
import org.wit.gastrograbs.helpers.showImagePicker
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
//import timber.log.Timber
import timber.log.Timber.i



class GrabActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrabBinding
    var grab = GrabModel()  //creating grab as a class member
    lateinit var app : MainApp

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityGrabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Grab Activity started..")

        if (intent.hasExtra("grab_edit")) {
            edit = true
            grab = intent.extras?.getParcelable("grab_edit")!!
            binding.grabTitle.setText(grab.title)
            binding.grabDescription.setText(grab.description)
            binding.grabCategory.setText(grab.category)
            binding.btnAdd.setText(R.string.save_grab)
            Picasso.get()
                .load(grab.image)
                .into(binding.grabImage)
            if (grab.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_grab_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            grab.title = binding.grabTitle.text.toString()
            grab.description = binding.grabDescription.text.toString()
            grab.category = binding.grabCategory.text.toString()
            if (grab.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_grab_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.grabs.update(grab.copy())
                } else {
                    app.grabs.create(grab.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
                   }
        registerImagePickerCallback()
     }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_grab, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            grab.image = result.data!!.data!!
                            Picasso.get()
                                .load(grab.image)
                                .into(binding.grabImage)
                            binding.chooseImage.setText(R.string.change_grab_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}