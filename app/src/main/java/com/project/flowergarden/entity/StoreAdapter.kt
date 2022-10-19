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

    private lateinit var OwnerDB: DatabaseReference
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
            openTime.text = ownerEntity.opentime
            closeTime.text = ownerEntity.closetime
            openDay.text = ownerEntity.openday

            OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
            OwnerDB.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val email = i.child("email").value
                        Log.d("이메일 결과", "$email")
                        val storage = FirebaseStorage.getInstance()
                        val storageReference = storage.reference
                        val pathReference =
                            storageReference.child("images/").child(email.toString())
                        pathReference.downloadUrl.addOnSuccessListener {
                            storeImageView.setImageURI(it)
                            progressBar.visibility = View.GONE
                            Glide.with(context).load(it).into(storeImageView) // GlideApp 사용
                            if (storeImageView.isVisible) {
                                progressBar.visibility = View.GONE
                                false
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



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

    fun setData(ownerEntity: OwnerEntity,listener: (OwnerEntity) -> Unit) {
        storeList.add(ownerEntity)
        itemClickListener  = listener
        notifyDataSetChanged()
    }
}