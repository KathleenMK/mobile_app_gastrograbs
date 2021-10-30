package org.wit.gastrograbs.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.adapters.CommentAdapter
import org.wit.gastrograbs.adapters.CommentDeleteAdapter
import org.wit.gastrograbs.adapters.CommentListener
import org.wit.gastrograbs.databinding.ActivityGrabBinding
import org.wit.gastrograbs.helpers.showImagePicker
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.Location
import timber.log.Timber.i



class GrabActivity : AppCompatActivity(), CommentListener {

    private lateinit var binding: ActivityGrabBinding
    var grab = GrabModel()  //creating grab as a class member
    lateinit var app : MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGrabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.setTitle(R.string.title_add)
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Grab Activity started..")

        if (intent.hasExtra("grab_edit")) {
            edit = true
            grab = intent.extras?.getParcelable("grab_edit")!!
            showGrab()

        }

        binding.btnAdd.setOnClickListener() {
            grab.title = binding.grabTitle.text.toString()
            grab.description = binding.grabDescription.text.toString()

            if(binding.categorySpinner.selectedItemPosition != 0){
                grab.category = binding.categorySpinner.selectedItem.toString()
            }
            else{
                grab.category = grab.category
            }
            if (grab.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_grab_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                  if (edit) {
                        app.grabs.update(grab.copy())
                      //i("these are the grab comments in GrabActivity")
                      //i(grab.comments.toString())
                    } else {
                        app.grabs.create(grab.copy())
                    }

                setResult(RESULT_OK)
                finish()
             }

        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
                   }

        binding.addLocation.setOnClickListener {
            var location = Location(52.15859, -7.14440, 16f)
            if (grab.zoom != 0f){
                location.lat = grab.lat
                location.lng = grab.lng
                location.zoom = grab.zoom
            }
            val launcherIntent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        val spinner: Spinner = findViewById(R.id.category_spinner)  //https://developer.android.com/guide/topics/ui/controls/spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        registerImagePickerCallback()
        registerMapCallback()
        registerRefreshCallback()
     }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_grab, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }

            R.id.item_delete -> { app.grabs.delete(grab)
            val launcherIntent = Intent(this, GrabCollectionActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            grab.lat = location.lat
                            grab.lng = location.lng
                            grab.zoom = location.zoom
                            binding.addLocation.setText(R.string.change_grab_location)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            binding.grabImage.visibility=View.VISIBLE
                            grab.image = result.data!!.data!!
                            Picasso.get()
                                .load(grab.image)
                                .into(binding.grabImage)
                            binding.chooseImage.setText(R.string.change_grab_image)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onCommentClick(comment: String) {
        i("in new listener")
        app.grabs.removeComment(grab,comment)
        showGrab()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { showGrab()
            }
    }

    private fun showGrab(){ // find updated grab from json after update
        var foundGrab = app.grabs.findOne(grab.id)
        i("show grab is +${foundGrab.toString()}")
        if (foundGrab != null) {
            binding.grabTitle.setText(foundGrab.title)
            binding.grabDescription.setText(foundGrab.description)
            binding.grabCategory.visibility= View.VISIBLE
            binding.grabCategory.setText(foundGrab.category)
            binding.btnAdd.setText(R.string.save_grab)
            binding.toolbarAdd.setTitle(R.string.title_update)

            Picasso.get()
                .load(foundGrab.image)
                .into(binding.grabImage)
            if (foundGrab.image != Uri.EMPTY) {
                binding.grabImage.visibility=View.VISIBLE
                binding.chooseImage.setText(R.string.change_grab_image)
            }
            if (foundGrab.comments.size > 0) {
                binding.commentHeader.visibility=View.VISIBLE
            }
            if (foundGrab.zoom != 0f){
                binding.addLocation.setText(R.string.change_grab_location)
            }
            val layoutManager = LinearLayoutManager(this)
            binding.recyclerViewComment.layoutManager = layoutManager
            binding.recyclerViewComment.adapter = CommentDeleteAdapter(foundGrab.comments.asReversed(),this)



        }
    }


}

