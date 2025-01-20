package com.example.adminstreethub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminstreethub.databinding.ActivitySignInBinding
import com.example.adminstreethub.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Configure Google Sign-In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Initialize Google Sign-In Client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Navigate to Sign-Up Page if user clicks "Don't Have an Account?"
        binding.dontHaveAccountButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Handle login button click
        binding.loginButton.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        // Handle Google Sign-In Button Click
        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                goToMainPage(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Account Created & Logged In Successfully", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        goToMainPage(user)
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "Sign In To Account: Failure", task.exception)
                    }
                }
            }
        }
    }



    // Save user data to Firebase Database
    private fun saveUserData() {
        val userId = auth.currentUser?.uid ?: return
        val user = UserModel(userName, email, password, nameOfRestaurant)

        database.child("users").child(userId).setValue(user)
    }

    // Google Sign-In Launcher
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful) {
                    val account : GoogleSignInAccount = task.result
                    firebaseAuthWithGoogle(account)
            } else {
                Log.e("GoogleSignIn", "Google sign in failed", task.exception)
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Authenticate with Firebase using Google credentials
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                Toast.makeText(this, "Signed In With Google", Toast.LENGTH_SHORT).show()
                goToMainPage(authTask.result?.user)
                finish()
            } else {
                Log.e("GoogleAuth", "Firebase authentication failed", authTask.exception)
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currUser = auth.currentUser
        if(currUser!=null){
            goToMainPage(currUser)
        }
    }

    override fun onDestroy() {
        googleSignInClient.signOut() // Ensures the session is properly closed
        super.onDestroy()
    }


    private fun goToMainPage(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
