package com.example.adminstreethub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.adminstreethub.databinding.ActivitySignUpBinding
import com.example.adminstreethub.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {


    private lateinit var userName : String
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var nameOfRestaurant : String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Firebase initialization
        auth = Firebase.auth
        // initialize firebase database
        database=Firebase.database.reference

        val locationList: Array<String> = arrayOf("Gudivada", "Vijayawada", "Guntur")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        // Handle button click to navigate to SignIn through Already Have An Account Text In SignUp Page
        binding.alreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent) // Corrected method call
        }

        binding.createButton.setOnClickListener{
            // getting data entered
            userName = binding.name.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            nameOfRestaurant = binding.restaurantName.text.toString().trim()
            
            if(userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
            else{
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent) // Corrected method call
                finish()
            }
            else{
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    // save data to database
    private fun saveUserData() {
        userName = binding.name.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        nameOfRestaurant = binding.restaurantName.text.toString().trim()
        val user = UserModel(userName, email, password, nameOfRestaurant)
        // to get the user id
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // Store the user data under the "users" node with the given userId in database
        database.child("user").child(userId).setValue(user)

    }
}