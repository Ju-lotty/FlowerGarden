package com.project.flowergarden.entity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.flowergarden.databinding.ItemStoreBinding

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    val storeList = mutableListOf<OwnerEntity>()

    inner class ViewHolder(private val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ownerEntity: OwnerEntity) = with(binding) {
            storeName.text = ownerEntity.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAdapter.ViewHolder {
        val view = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreAdapter.ViewHolder, position: Int) {
        holder.bind(storeList[position])
    }

    override fun getItemCount(): Int = storeList.size
}