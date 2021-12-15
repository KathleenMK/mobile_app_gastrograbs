package org.wit.gastrograbs.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.wit.gastrograbs.R
import org.wit.gastrograbs.activities.GastroGrabs
import org.wit.gastrograbs.databinding.ActivityGrabberLoginBinding
import timber.log.Timber

class GrabberLogin : AppCompatActivity() {

    private lateinit var loginRegisterViewModel : LoginRegisterViewModel
    private lateinit var loginBinding : ActivityGrabberLoginBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityGrabberLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.emailSignInButton.setOnClickListener {
            signIn(loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString())
        }
        loginBinding.emailCreateAccountButton.setOnClickListener {
            createAccount(loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loginRegisterViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser -> if (firebaseUser != null)
            startActivity(Intent(this, GastroGrabs::class.java)) })

        loginRegisterViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
        { status -> checkStatus(status) })
    }

    //Required to exit app from Login Screen - must investigate this further
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Click again to Close App...",Toast.LENGTH_LONG).show()
        finish()
    }

    private fun createAccount(email: String, password: String) {
        Timber.d("createAccount:$email")
        if (!validateForm()) { return }

        loginRegisterViewModel.register(email,password)
    }

    private fun signIn(email: String, password: String) {
        Timber.d("signIn:$email")
        if (!validateForm()) { return }

        loginRegisterViewModel.login(email,password)
    }

    private fun checkStatus(error:Boolean) {
        if (error)
            Toast.makeText(this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = loginBinding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginBinding.fieldEmail.error = "Required."
            valid = false
        } else {
            loginBinding.fieldEmail.error = null
        }

        val password = loginBinding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginBinding.fieldPassword.error = "Required."
            valid = false
        } else {
            loginBinding.fieldPassword.error = null
        }
        return valid
    }
}

//class GrabberLogin : AppCompatActivity(), View.OnClickListener {
//
//    // [START declare_auth]
//    private lateinit var auth: FirebaseAuth
//    // [END declare_auth]
//    lateinit var loader : AlertDialog
//    private lateinit var loginBinding : ActivityGrabberLoginBinding
//
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        loginBinding = ActivityGrabberLoginBinding.inflate(layoutInflater)
//        setContentView(loginBinding.root)
//
//        // Buttons
//        loginBinding.emailSignInButton.setOnClickListener(this)
//        loginBinding.emailCreateAccountButton.setOnClickListener(this)
//        loginBinding.signOutButton.setOnClickListener(this)
//        loginBinding.verifyEmailButton.setOnClickListener(this)
//
//        // [START initialize_auth]
//        // Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance()
//        // [END initialize_auth]
//
//        loader = createLoader(this)
//    }
//
//    // [START on_start_check_user]
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }
//    // [END on_start_check_user]
//
//    private fun createAccount(email: String, password: String) {
//        Timber.d( "createAccount:$email")
//        if (!validateForm()) {
//            return
//        }
//
//        showLoader(loader, "Creating Account...")
//
//        // [START create_user_with_email]
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Timber.d( "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Timber.w( "createUserWithEmail:failure $task.exception")
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//
//                // [START_EXCLUDE]
//                hideLoader(loader)
//                // [END_EXCLUDE]
//            }
//        // [END create_user_with_email]
//    }
//
//    private fun signIn(email: String, password: String) {
//        Timber.d( "signIn:$email")
//        if (!validateForm()) {
//            return
//        }
//
//        showLoader(loader, "Logging In...")
//
//        // [START sign_in_with_email]
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Timber.d( "signInWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Timber.w( "signInWithEmail:failure $task.exception")
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//
//                // [START_EXCLUDE]
//                if (!task.isSuccessful) {
//                    loginBinding.status.setText(R.string.auth_failed)
//                }
//                hideLoader(loader)
//                // [END_EXCLUDE]
//            }
//        // [END sign_in_with_email]
//    }
//
//    private fun signOut() {
//        auth.signOut()
//        updateUI(null)
//    }
//
//    private fun sendEmailVerification() {
//        // Disable button
//        loginBinding.verifyEmailButton.isEnabled = false
//
//        // Send verification email
//        // [START send_email_verification]
//        val user = auth.currentUser
//        user?.sendEmailVerification()
//            ?.addOnCompleteListener(this) { task ->
//                // [START_EXCLUDE]
//                // Re-enable button
//                loginBinding.verifyEmailButton.isEnabled = true
//
//                if (task.isSuccessful) {
//                    Toast.makeText(baseContext,
//                        "Verification email sent to ${user.email} ",
//                        Toast.LENGTH_SHORT).show()
//                } else {
//                    Timber.e( "sendEmailVerification $task.exception")
//                    Toast.makeText(baseContext,
//                        "Failed to send verification email.",
//                        Toast.LENGTH_SHORT).show()
//                }
//                // [END_EXCLUDE]
//            }
//        // [END send_email_verification]
//    }
//
//    private fun validateForm(): Boolean {
//        var valid = true
//
//        val email = loginBinding.fieldEmail.text.toString()
//        if (TextUtils.isEmpty(email)) {
//            loginBinding.fieldEmail.error = "Required."
//            valid = false
//        } else {
//            loginBinding.fieldEmail.error = null
//        }
//
//        val password = loginBinding.fieldPassword.text.toString()
//        if (TextUtils.isEmpty(password)) {
//            loginBinding.fieldPassword.error = "Required."
//            valid = false
//        } else {
//            loginBinding.fieldPassword.error = null
//        }
//
//        return valid
//    }
//
//    private fun updateUI(user: FirebaseUser?) {
//        hideLoader(loader)
//        if (user != null) {
//            loginBinding.status.text = getString(R.string.emailpassword_status_fmt,
//                user.email, user.isEmailVerified)
//            loginBinding.detail.text = getString(R.string.firebase_status_fmt, user.uid)
//
//            loginBinding.emailPasswordButtons.visibility = View.GONE
//            loginBinding.emailPasswordFields.visibility = View.GONE
//            loginBinding.signedInButtons.visibility = View.VISIBLE
//
//            loginBinding.verifyEmailButton.isEnabled = !user.isEmailVerified
//        } else {
//            loginBinding.status.setText(R.string.signed_out)
//            loginBinding.detail.text = null
//
//            loginBinding.emailPasswordButtons.visibility = View.VISIBLE
//            loginBinding.emailPasswordFields.visibility = View.VISIBLE
//            loginBinding.signedInButtons.visibility = View.GONE
//        }
//    }
//
//    override fun onClick(v: View) {
//        val i = v.id
//        when (i) {
//            R.id.emailCreateAccountButton -> createAccount(loginBinding.fieldEmail.text.toString(), loginBinding.fieldPassword.text.toString())
//            R.id.emailSignInButton -> signIn(loginBinding.fieldEmail.text.toString(), loginBinding.fieldPassword.text.toString())
//            R.id.signOutButton -> signOut()
//            R.id.verifyEmailButton -> sendEmailVerification()
//        }
//    }
//
//    companion object {
//        private const val TAG = "EmailPassword"
//    }
//}
//
////{
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_grabber_login)
////    }
////}

