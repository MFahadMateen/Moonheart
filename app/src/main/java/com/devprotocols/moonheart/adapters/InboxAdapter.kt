package com.devprotocols.moonheart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.activities.ChatActivity
import com.devprotocols.moonheart.databinding.RvInboxItemBinding
import com.devprotocols.moonheart.models.ChatModel
import com.devprotocols.moonheart.models.UserModel
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InboxAdapter(var context: Context) :
    RecyclerView.Adapter<InboxAdapter.ViewHolder>() {
    private var chatModelList = ArrayList<ChatModel>()

    inner class ViewHolder(val binding: RvInboxItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvInboxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(chatModelList[position]) {
                binding.txtLastMessage.text = this.lastMessage
                val otherPersonId =
                    if (this.members!![0] == FirebaseAuth.getInstance().uid) this.members!![1] else this.members!![0]
                var otherUser = UserModel()
                FirebaseDatabase.getInstance().getReference(Constants.users).child(otherPersonId)
                    .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                otherUser = snapshot.getValue(UserModel::class.java)!!
                                Glide.with(context).load(otherUser.image)
                                    .placeholder(R.drawable.placeholder).into(binding.imgProfile)
                                binding.txtName.text = otherUser.name
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        }
                    )
                itemView.setOnClickListener {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("userId", otherPersonId)
                    intent.putExtra("name", otherUser.name)
                    intent.putExtra("image", otherUser.image)
                    context.startActivity(intent)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return chatModelList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(chatList: ArrayList<ChatModel>) {
        chatModelList = ArrayList()
        chatModelList = chatList
        notifyDataSetChanged()
    }
}