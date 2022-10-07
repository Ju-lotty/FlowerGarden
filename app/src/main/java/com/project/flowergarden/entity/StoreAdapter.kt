package com.project.flowergarden.entity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.flowergarden.databinding.ItemStoreBinding

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    val storeList = mutableListOf<OwnerEntity>()
    lateinit var itemClickListener: (OwnerEntity) -> Unit

    inner class ViewHolder(
        private val binding: ItemStoreBinding,
        private val listener: (OwnerEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ownerEntity: OwnerEntity) = with(binding) {
            storeName.text = ownerEntity.storename
            address.text = ownerEntity.address


            root.setOnClickListener {
                listener(ownerEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAdapter.ViewHolder {
        val view = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: StoreAdapter.ViewHolder, position: Int) {
        holder.bind(storeList[position])
    }

    override fun getItemCount(): Int = storeList.size

    fun setData(ownerEntity: OwnerEntity,listener: (OwnerEntity) -> Unit){
        storeList.add(ownerEntity)
        itemClickListener  = listener
        notifyDataSetChanged()
    }
}