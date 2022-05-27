package com.devprotocols.moonheart.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devprotocols.moonheart.adapters.UsersAdapter
import com.devprotocols.moonheart.databinding.FragmentUsersBinding
import com.devprotocols.moonheart.models.UserModel
import com.devprotocols.moonheart.utils.BaseUtils
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD
import java.util.*
import kotlin.collections.ArrayList


class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private var filterList = java.util.ArrayList<UserModel>()
    private lateinit var usersAdapter: UsersAdapter
    private var userList = ArrayList<UserModel>()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog:KProgressHUD
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        binding.rvUsers.setHasFixedSize(true)
        dialog = BaseUtils.progressDialog(requireContext())
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        usersAdapter = UsersAdapter(requireContext())
        binding.rvUsers.adapter = usersAdapter
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchFromList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        getAllUsers()
    }

    private fun getAllUsers() {
        dialog.show()
        FirebaseDatabase.getInstance().getReference(Constants.users).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(UserModel::class.java)
                    if (user != null && user.userId != mAuth.uid) {
                        userList.add(user)
                    }
                }
                dialog.dismiss()
                usersAdapter.setList(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }

        })
    }
    private fun searchFromList(keyword: String) {
        filterList = java.util.ArrayList()
        for (i in userList.indices) {
            if (userList[i].name!!.lowercase(Locale.ROOT)
                    .contains(keyword.lowercase(Locale.ROOT))
            ) {
                filterList.add(userList[i])
            }
        }
        usersAdapter.setList(filterList)
    }
}