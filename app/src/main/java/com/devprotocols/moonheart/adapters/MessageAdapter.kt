package com.devprotocols.moonheart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.models.MessageModel
import com.devprotocols.moonheart.utils.BaseUtils
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(var context: Context) :
    RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
    private var messageList: List<MessageModel> = ArrayList()
    private var typeLeft = 0
    private var typeRight = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if (viewType == typeRight) {
            MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_chat_right, parent, false)
            )
        } else {
            MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_chat_left, parent, false)
            )
        }
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.txtMessage.text = messageList[position].message
        holder.txtTime.text = BaseUtils.getTime(messageList[position].time!!)
    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(messageModelList: List<MessageModel>) {
        messageList = ArrayList()
        messageList = messageModelList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtMessage: TextView
        var txtTime: TextView
//        var imgProfile: CircleImageView

        init {
            txtMessage = itemView.findViewById(R.id.txt_message)
            txtTime = itemView.findViewById(R.id.txt_time)
//            imgProfile = itemView.findViewById(R.id.img_profile)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].sendBy == FirebaseAuth.getInstance().uid.toString()) {
            typeRight
        } else {
            typeLeft
        }
    }
}