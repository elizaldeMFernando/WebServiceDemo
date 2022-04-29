package com.example.webservicedemo

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.webservicedemo.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val binding = ItemDogBinding.bind(view)
    private val v:View= view
    fun bind(image:String){
        Picasso.get().load(image).into(binding.ivDog)
        v.findViewById<ImageView>(R.id.ivDog)
    }
}