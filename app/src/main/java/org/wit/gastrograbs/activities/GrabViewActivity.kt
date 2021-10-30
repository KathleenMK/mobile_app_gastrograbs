package org.wit.gastrograbs.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.adapters.CommentAdapter
import org.wit.gastrograbs.databinding.ActivityGrabViewBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.Location
import timber.log.Timber.i

class GrabViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrabViewBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    var grab = GrabModel()  //creating grab as a class member
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityGrabViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewComment.layoutManager = layoutManager

        grab = intent.extras?.getParcelable("grab_view")!!
        i("in onCreate view for + ${grab.title}")

        binding.btnAddComment.setOnClickListener{
           var newComment = binding.newComment.text.toString()
            if (newComment.isEmpty()) {
                Snackbar.make(it,R.string.empty_comment, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                app.grabs.addComment(grab,newComment)
                Snackbar.make(it,R.string.added_comment, Snackbar.LENGTH_LONG)
                    .show()
                showGrab()
                binding.newComment.setText("")
            }
        }

        binding.btnViewMap.setOnClickListener {
            var location = Location(52.15859, -7.14440, 16f)
            if (grab.zoom != 0f){
                location.lat = grab.lat
                location.lng = grab.lng
                location.zoom = grab.zoom
            }
            val launcherIntent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            refreshIntentLauncher.launch(launcherIntent)
        }

        showGrab()
        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_view, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {   showGrab()
          }
    }

    private fun showGrab(){ // find updated grab from json after update
        var foundGrab = app.grabs.findOne(grab.id)
        i("show grab is +${foundGrab.toString()}")
        if (foundGrab != null) {
            binding.toolbarAdd.title = foundGrab.title
            setSupportActionBar(binding.toolbarAdd)
            if(foundGrab.description.length > 0) {
                binding.grabDescription.visibility=View.VISIBLE
                binding.grabDescription.setText(foundGrab.description)
            }
            if(foundGrab.description.length > 0) {
                binding.grabCategory.visibility = View.VISIBLE
                binding.grabCategory.setText(foundGrab.category)
            }
            if (foundGrab.image != Uri.EMPTY) {
                binding.grabImage.visibility = View.VISIBLE
                Picasso.get()
                    .load(foundGrab.image)
                    .into(binding.grabImage)
            }
            if (foundGrab.zoom != 0f){
                binding.btnViewMap.visibility = View.VISIBLE
            }
            //i("these are the grab comments in View+${foundGrab.comments.toString()}")
            binding.recyclerViewComment.adapter = CommentAdapter(foundGrab.comments.asReversed())
                    }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.item_cancel -> { finish() }

            R.id.item_edit -> {val launcherIntent = Intent(this, GrabActivity::class.java)
            launcherIntent.putExtra("grab_edit",grab)
                refreshIntentLauncher.launch(launcherIntent)}
        }
        return super.onOptionsItemSelected(item)
    }



}