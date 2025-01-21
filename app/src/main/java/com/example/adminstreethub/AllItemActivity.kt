package com.example.adminstreethub

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminstreethub.adapter.MenuItemAdapter
import com.example.adminstreethub.databinding.ActivityAllItemBinding
import com.example.adminstreethub.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllMenu> = ArrayList()

    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItems()

        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun retrieveMenuItems() {
        database=FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")

        // fetch the data
        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                // clear existing data before populating
                menuItems.clear()

                // looping through each food item
                for(foodSnapShot in snapshot.children){
                    val menuItem = foodSnapShot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Database Error", "Error: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllItemActivity, menuItems, databaseReference)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerView.adapter = adapter
    }


}