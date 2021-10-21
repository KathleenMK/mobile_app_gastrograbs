package org.wit.gastrograbs.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivityGrabViewBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
import timber.log.Timber.i

class GrabViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrabViewBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
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

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_view, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.item_cancel -> { finish() }

            R.id.item_edit -> {val launcherIntent = Intent(this, GrabActivity::class.java)
            launcherIntent.putExtra("grab_edit",grab)
                refreshIntentLauncher.launch(launcherIntent)}

            R.id.item_delete -> { app.grabs.delete(grab)
                                    setResult(RESULT_OK)
                                    finish()}
        }
        return super.onOptionsItemSelected(item)
    }



    

}