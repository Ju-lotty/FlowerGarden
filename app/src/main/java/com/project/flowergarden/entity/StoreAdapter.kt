package com.project.flowergarden.entity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import com.project.flowergarden.databinding.ItemStoreBinding
import kotlinx.android.synthetic.main.activity_main_owner.*

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private val OwnerDB: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Owner").apply {
            addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEachIndexed { index, i ->
                        val email = i.child("email").value.toString()
                        emailList.add(email)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    private val emailList = mutableListOf<String>()
    val storeList = mutableListOf<OwnerEntity>()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    lateinit var itemClickListener: (OwnerEntity) -> Unit

    inner class ViewHolder(
        private val binding: ItemStoreBinding,
        private val listener: (OwnerEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ownerEntity: OwnerEntity, index: Int) = with(binding) {
            storeName.text = ownerEntity.storename
            address.text = ownerEntity.address
            openTime.text = ownerEntity.opentime
            closeTime.text = ownerEntity.closetime
            openDay.text = ownerEntity.openday
            val email = emailList[index]
            val pathReference = storageReference.child("images/").child(email)
            pathReference.downloadUrl.addOnSuccessListener {
                storeImageView.setImageURI(it)
                progressBar.visibility = View.GONE
                Glide.with(context).load(it).into(storeImageView) // GlideApp 사용
                if (storeImageView.isVisible) {
                    progressBar.visibility = View.GONE
                    false
                }
            }
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
        holder.bind(storeList[position], position)
    }

    override fun getItemCount(): Int = storeList.size

    fun setData(ownerEntity: OwnerEntity, listener: (OwnerEntity) -> Unit) {
        storeList.add(ownerEntity)
        itemClickListener = listener
        notifyDataSetChanged()
    }
}