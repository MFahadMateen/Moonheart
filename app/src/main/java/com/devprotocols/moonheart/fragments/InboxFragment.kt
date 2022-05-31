package com.devprotocols.moonheart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devprotocols.moonheart.adapters.InboxAdapter
import com.devprotocols.moonheart.databinding.FragmentInboxBinding
import com.devprotocols.moonheart.models.ChatModel
import com.devprotocols.moonheart.utils.BaseUtils
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD

class InboxFragment : Fragment() {

    private lateinit var binding: FragmentInboxBinding
    private lateinit var inboxAdapter: InboxAdapter
    private var chatList = ArrayList<ChatModel>()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: KProgressHUD
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        binding.rvChats.setHasFixedSize(true)
        dialog = BaseUtils.progressDialog(requireContext())
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        inboxAdapter = InboxAdapter(requireContext())
        binding.rvChats.adapter = inboxAdapter
        getAllChats()
    }

    private fun getAllChats() {
        dialog.show()
        FirebaseDatabase.getInstance().getReference(Constants.chats).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList = ArrayList()
                    for (ds in snapshot.children) {
                        val chatModel = ds.getValue(ChatModel::class.java)
                        if (chatModel!!.members!!.contains(mAuth.uid.toString())) {
                            chatList.add(chatModel)
                        }
                    }
                    if (chatList.size > 0)
                        binding.animationView.visibility = View.GONE
                    else
                        binding.animationView.visibility = View.VISIBLE

                    chatList.sortByDescending { it.timestamp }
                    inboxAdapter.setList(chatList)
                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                }

            }
        )
    }
}