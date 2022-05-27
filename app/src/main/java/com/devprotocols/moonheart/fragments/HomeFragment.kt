package com.devprotocols.moonheart.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.activities.EditProfileActivity
import com.devprotocols.moonheart.databinding.FragmentHomeBinding
import com.devprotocols.moonheart.models.UserModel
import com.devprotocols.moonheart.utils.BaseUtils
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: KProgressHUD
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        dialog = BaseUtils.progressDialog(requireContext())
        binding.btnEdit.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    EditProfileActivity::class.java
                )
            )
        }
        getDataFromServer()

    }

    private fun getDataFromServer() {
        dialog.show()
        FirebaseDatabase.getInstance().getReference(Constants.users).child(mAuth.uid.toString()).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                binding.etName.setText(user?.name)
                binding.etEmail.setText(user?.email)
                binding.etAge.setText(user?.age)
                Glide.with(requireContext()).load(user?.image).placeholder(R.drawable.placeholder).into(binding.imgProfile)
                Glide.with(requireContext()).load(user?.cover).placeholder(R.drawable.cover).into(binding.imgCover)
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}