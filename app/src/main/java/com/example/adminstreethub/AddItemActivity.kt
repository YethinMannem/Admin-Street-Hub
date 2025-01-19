package com.example.adminstreethub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminstreethub.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // image popup
        binding.selectImage.setOnClickListener{
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.backButton.setOnClickListener{
            finish()
        }

    }
    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri->
        if(uri!=null){
            binding.selectedImage.setImageURI(uri)
        }
    }
}