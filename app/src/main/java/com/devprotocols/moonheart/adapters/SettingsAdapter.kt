package com.devprotocols.moonheart.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.activities.LoginActivity
import com.devprotocols.moonheart.databinding.RvSettingsItemBinding
import com.devprotocols.moonheart.models.SettingsModel
import com.google.firebase.auth.FirebaseAuth

class SettingsAdapter(var context: Context) :
    RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    private var settingsModelList = ArrayList<SettingsModel>()

    inner class ViewHolder(val binding: RvSettingsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvSettingsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(settingsModelList[position]) {
                binding.imgIcon.setImageResource(this.image!!)
                binding.txtName.text = this.name
                itemView.setOnClickListener {
                    when (position) {
                        0 -> {

                        }
                        1 -> {
                        }
                        2 -> {
                        }
                        3 -> {
                            showAlertDialog()
                        }
                    }
                }
            }
        }
    }

    private fun showAlertDialog() {
        lateinit var alertDialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        //set message for alert dialog
        builder.setMessage(R.string.logout_message)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            alertDialog.dismiss()
            (context as Activity).finishAffinity()
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
            alertDialog.dismiss()
        }
        // Create the AlertDialog
        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun getItemCount(): Int {
        return settingsModelList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(settingsList: ArrayList<SettingsModel>) {
        settingsModelList = ArrayList()
        settingsModelList = settingsList
        notifyDataSetChanged()
    }
}