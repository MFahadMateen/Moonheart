package com.devprotocols.moonheart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.activities.UserProfileActivity
import com.devprotocols.moonheart.databinding.RvUsersItemBinding
import com.devprotocols.moonheart.models.UserModel

class UsersAdapter(var context: Context) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private var userModelList = ArrayList<UserModel>()

    inner class ViewHolder(val binding: RvUsersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvUsersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(userModelList[position]) {
                Glide.with(context).load(this.image).placeholder(R.drawable.placeholder).into(binding.imgProfile)
                binding.txtName.text = this.name
                itemView.setOnClickListener{
                    val intent = Intent(context, UserProfileActivity::class.java)
                    intent.putExtra("userId",this.userId)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userModelList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(userList: ArrayList<UserModel>) {
        userModelList = ArrayList()
        userModelList = userList
        notifyDataSetChanged()
    }
}