package com.example.adminstreethub.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminstreethub.databinding.ItemMenuBinding
import com.example.adminstreethub.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference
):
    RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(menuList.size) { 1 }
//    private val itemClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // this code help in getting the description page for the items
//        init {
//            binding.root.setOnClickListener{
//                val position = adapterPosition
//                if(position != RecyclerView.NO_POSITION){
//                    itemClickListener?.onItemClick(position)
//                }

        // set on click listener to open details
//                val intent = Intent(requireContext, DetailsActivity::class.java)
//                intent.putExtra("MenuItemName", menuItemName.get(position))
//                intent.putExtra("MenuItemImage", menuItemImage.get(position))
//                requireContext.startActivity(intent)
//            }
//        }
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                allmenuFoodName.text = menuItem.foodName
                allMenuFoodPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(allMenuFoodImage)
                allMenuFoodQuantity.text=quantity.toString()
                minusButton.setOnClickListener() {
                    decreaseQuantity(position)
                }

                plusButton.setOnClickListener() {
                    increaseQuantity(position)
                }

                deleteButton.setOnClickListener() {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] >= 1) {
                itemQuantities[position]--;
                binding.allMenuFoodQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 20) {
                itemQuantities[position]++;
                binding.allMenuFoodQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }
    }
}


