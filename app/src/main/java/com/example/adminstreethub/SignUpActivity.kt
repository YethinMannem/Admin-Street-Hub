package com.example.adminstreethub

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminstreethub.databinding.ActivitySignInBinding
import com.example.adminstreethub.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Corrected method call
        }
    }
}