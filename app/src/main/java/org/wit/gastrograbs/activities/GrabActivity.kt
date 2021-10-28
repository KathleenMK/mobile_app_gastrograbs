package org.wit.gastrograbs.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
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
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityGrabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.setTitle(R.string.title_add)
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
            binding.toolbarAdd.setTitle(R.string.title_update)

            Picasso.get()
                .load(grab.image)
                .into(binding.grabImage)
            if (grab.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_grab_image)
            }
            val layoutManager = LinearLayoutManager(this)
            binding.recyclerViewComment.layoutManager = layoutManager
            binding.recyclerViewComment.adapter = CommentDeleteAdapter(grab.comments.asReversed(),this)



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
        //binding.recyclerViewComment.adapter.    //setBackgroundColor(getColor(R.color.colorPrimaryDark))
        app.grabs.removeComment(grab,comment)
        i("${comment} will be deleted")
        binding.recyclerViewComment.adapter?.notifyDataSetChanged()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewComment.layoutManager = layoutManager
        binding.recyclerViewComment.adapter = CommentDeleteAdapter(grab.comments.asReversed(),this)
    }


}