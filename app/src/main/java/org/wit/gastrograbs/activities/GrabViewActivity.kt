package org.wit.gastrograbs.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivityGrabViewBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber.i

class GrabViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrabViewBinding
    var grab = GrabModel()  //creating grab as a class member
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i("in onCreate view")
        i(grab.title)
        binding = ActivityGrabViewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        app = application as MainApp

        if (intent.hasExtra("grab_view")) {
            //edit = true
            grab = intent.extras?.getParcelable("grab_view")!!

            binding.toolbarAdd.title = grab.title
            setSupportActionBar(binding.toolbarAdd)

            //binding.grabTitle.setText(grab.title)
            binding.grabDescription.setText(grab.description)
            binding.grabCategory.setText(grab.category)
            //binding.btnAdd.setText(R.string.save_grab)
            Picasso.get()
                .load(grab.image)
                .into(binding.grabImage)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_grab, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.item_cancel -> { finish() }

            R.id.item_edit -> {val launcherIntent = Intent(this, GrabActivity::class.java)
            launcherIntent.putExtra("grab_edit",grab)
                    startActivityForResult(launcherIntent,0)}
        }
        return super.onOptionsItemSelected(item)
    }



    

}