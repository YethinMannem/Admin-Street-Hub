package com.example.adminstreethub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminstreethub.databinding.ItemMenuBinding

class AddItemAdapter(private val menuItemName: MutableList<String>, private val menuItemPrice: MutableList<String>, private val menuItemImage: MutableList<Int>):
    RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(menuItemName.size) { 1 }
//    private val itemClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItemName.size

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
                allmenuFoodName.text = menuItemName[position]
                allMenuFoodPrice.text = menuItemPrice[position]
                allMenuFoodImage.setImageResource(menuItemImage[position])

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
            menuItemName.removeAt(position)
            menuItemImage.removeAt(position)
            menuItemPrice.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuItemName.size)
        }
    }
}


