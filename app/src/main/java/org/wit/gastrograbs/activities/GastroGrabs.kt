package org.wit.gastrograbs.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseUser
import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivityGastroGrabsBinding
import org.wit.gastrograbs.databinding.NavHeaderGastroGrabsBinding
import org.wit.gastrograbs.ui.auth.GrabberLogin
import org.wit.gastrograbs.ui.auth.LoggedInViewModel

class GastroGrabs : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGastroGrabsBinding

    private lateinit var drawerLayout: DrawerLayout
    //private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderGastroGrabsBinding
    private lateinit var loggedInViewModel : LoggedInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGastroGrabsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_gastro_grabs)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = binding.navView
        navView.setupWithNavController(navController)
    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, GrabberLogin::class.java))
            }
        })

    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityGastroGrabsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        drawerLayout = ActivityGastroGrabsBinding.drawerLayout
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        setSupportActionBar(binding.appBarGastroGrabs.toolbar)
//
//        binding.appBarGastroGrabs.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_content_gastro_grabs)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, //R.id.nav_gallery, R.id.nav_slideshow   //only options here will display the hamburger menu, otherwise a back
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.gastro_grabs, menu)
//        return true
//    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        var headerView = binding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderGastroGrabsBinding.bind(headerView)
        navHeaderBinding.navHeaderEmail.text = currentUser.email
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_gastro_grabs)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, GrabberLogin::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}