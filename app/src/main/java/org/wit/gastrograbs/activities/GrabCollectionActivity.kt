package org.wit.gastrograbs.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrabCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        loadGrabs()
        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

       override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GrabActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGrabClick(grab: GrabModel) {
        val launcherIntent = Intent(this, GrabViewActivity::class.java)
        launcherIntent.putExtra("grab_view",grab)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadGrabs() }
    }

    private fun loadGrabs(){
        showGrabs(app.grabs.findAll())
    }

    fun showGrabs(grabs: List<GrabModel>){
        binding.recyclerView.adapter = GrabAdapter(grabs, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }


}

