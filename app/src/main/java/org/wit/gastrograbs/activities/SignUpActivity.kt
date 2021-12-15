package org.wit.gastrograbs.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivitySignUpBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabberModel
import org.wit.gastrograbs.ui.home.GastroGrabs
import timber.log.Timber

class SignUpActivity : AppCompatActivity() {

    lateinit var app: MainApp
    var grabber = GrabberModel()
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_sign_up)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        Timber.i("Sign Up Activity started..")

        binding.btnSignUp.setOnClickListener() {

            grabber.email = binding.editEmail.text.toString()
            grabber.password = binding.editPassword.text.toString()
            if (grabber.email.isEmpty() || grabber.password.isEmpty()) {
                Snackbar.make(it,R.string.enter_login, Snackbar.LENGTH_LONG)
                    .show()
            }
            else if(app.grabbers.findOneEmail(grabber.email))
            {
                binding.editEmail.setText("")
                binding.editPassword.setText("")
                Snackbar.make(it,R.string.signup_fail, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
               app.grabbers.signup(grabber.copy())
                val launcherIntent = Intent(this, GastroGrabs::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }

       registerRefreshCallback()

    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {            }
    }

}
