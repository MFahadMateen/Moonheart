package com.devprotocols.moonheart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.adapters.MessageAdapter
import com.devprotocols.moonheart.databinding.ActivityChatBinding
import com.devprotocols.moonheart.models.ChatModel
import com.devprotocols.moonheart.models.MessageModel
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var messageAdapter: MessageAdapter
    private var messageList = ArrayList<MessageModel>()
    private var otherPersonsUserId: String? = null
    private var otherPersonsName: String? = null
    private var otherPersonsImage: String? = null
    private var chatId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        otherPersonsUserId = intent.getStringExtra("userId")
        otherPersonsName = intent.getStringExtra("name")
        otherPersonsImage = intent.getStringExtra("image")
        mAuth = FirebaseAuth.getInstance()
        initViews()
        checkIfChatRoomExists()
        initClickListener()

    }

    private fun initViews() {
        binding.rvMessages.setHasFixedSize(true)
        binding.rvMessages.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(this)
        binding.rvMessages.adapter = messageAdapter
        binding.txtName.text = otherPersonsName
        Glide.with(this).load(otherPersonsImage).placeholder(R.drawable.placeholder).into(binding.imgProfile)
    }

    private fun checkIfChatRoomExists() {
        FirebaseDatabase.getInstance().getReference(Constants.chats).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val chatModel = ds.getValue(ChatModel::class.java)
                        if (chatModel!!.members!!.contains(otherPersonsUserId) && chatModel.members!!.contains(
                                mAuth.uid.toString()
                            )
                        ) {
                            chatId = chatModel.chatId
                            getAllMessages()
                            return
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
        )

    }

    private fun getAllMessages() {
        FirebaseDatabase.getInstance().getReference(Constants.chats).child(chatId!!)
            .child(Constants.messages).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList = ArrayList()
                    for (ds in snapshot.children) {
                        val messageModel = ds.getValue(MessageModel::class.java)
                        messageList.add(messageModel!!)
                    }
                    messageAdapter.setList(messageList)
                    binding.rvMessages.scrollToPosition(messageAdapter.itemCount - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
        )
    }

    private fun initClickListener() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.imgSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        binding.etMessage.setText("")
        if (chatId != null) {
            val ref = FirebaseDatabase.getInstance().getReference(Constants.chats).child(chatId!!)
                .child(Constants.messages).push()
            val messageModel =
                MessageModel(ref.key, message, mAuth.uid.toString(), System.currentTimeMillis())
            ref.setValue(messageModel)
            val hashMap = HashMap<String, Any>()
            hashMap["lastMessage"] = message
            hashMap["timestamp"] = System.currentTimeMillis()
            FirebaseDatabase.getInstance().getReference(Constants.chats).child(chatId!!)
                .updateChildren(hashMap)
        } else {
            val members = ArrayList<String>()
            members.add(otherPersonsUserId!!)
            members.add(mAuth.uid.toString())
            val ref = FirebaseDatabase.getInstance().getReference(Constants.chats).push()
            chatId = ref.key
            val messageRef =
                FirebaseDatabase.getInstance().getReference(Constants.chats).child(chatId!!)
                    .child(Constants.messages).push()
            val messageModel = MessageModel(
                messageRef.key,
                message,
                mAuth.uid.toString(),
                System.currentTimeMillis()
            )
            val chatModel = ChatModel(chatId, message,System.currentTimeMillis(), members)
            ref.setValue(chatModel)
            messageRef.setValue(messageModel)
            getAllMessages()
        }

    }
}