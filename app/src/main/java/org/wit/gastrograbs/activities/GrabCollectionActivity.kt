package org.wit.gastrograbs.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
//import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
import org.wit.gastrograbs.R
import org.wit.gastrograbs.adapters.GrabAdapter
import org.wit.gastrograbs.adapters.GrabListener
import org.wit.gastrograbs.databinding.ActivityGrabCollectionBinding
//import org.wit.gastrograbs.databinding.CardGrabBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel

//import org.wit.gastrograbs.models.GrabModel

class GrabCollectionActivity : AppCompatActivity(), GrabListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGrabCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrabCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GrabAdapter(app.grabs.findAll(),this)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

       override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GrabActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGrabClick(grab: GrabModel) {
        val launcherIntent = Intent(this, GrabActivity::class.java)
        launcherIntent.putExtra("grab_edit",grab)
        startActivityForResult(launcherIntent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}

