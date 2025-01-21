package com.example.adminstreethub

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminstreethub.databinding.ActivityAddItemBinding
import com.example.adminstreethub.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    // Food item details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private var foodImage: Uri? = null
    private lateinit var foodIngredients: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Firebase initialization
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Image selection
        binding.foodImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addItemButton.setOnClickListener {
            // Get data from fields
            foodName = binding.FoodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.foodDescription.text.toString().trim()
            foodIngredients = binding.foodIngredients.text.toString().trim()

            if (foodName.isNotBlank() && foodPrice.isNotBlank() && foodDescription.isNotBlank() && foodIngredients.isNotBlank()) {
                uploadData()
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImage = uri
        }
    }

    private fun uploadData() {
        val menuRef = database.child("menu")
        val newItemKey = menuRef.push().key

        if (newItemKey == null) {
            Toast.makeText(this, "Error generating item key", Toast.LENGTH_SHORT).show()
            return
        }

        if (foodImage != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/$newItemKey.jpg")
            val uploadTask = imageRef.putFile(foodImage!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredients = foodIngredients,
                        foodImage = downloadUrl.toString()
                    )

                    menuRef.child(newItemKey).setValue(newItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            finish() // Close activity after upload
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }
}
