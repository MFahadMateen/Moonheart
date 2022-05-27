package com.devprotocols.moonheart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.adapters.SettingsAdapter
import com.devprotocols.moonheart.databinding.FragmentSettingsBinding
import com.devprotocols.moonheart.models.SettingsModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsAdapter: SettingsAdapter
    private var settingsList = ArrayList<SettingsModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSettings.setHasFixedSize(true)
        binding.rvSettings.layoutManager = LinearLayoutManager(requireContext())
        settingsAdapter = SettingsAdapter(requireContext())
        binding.rvSettings.adapter = settingsAdapter
        setList()
    }

    private fun setList() {
        settingsList = ArrayList()
        settingsList.add(
            SettingsModel(
                R.drawable.ic_baseline_share_24,
                resources.getString(R.string.share)
            )
        )
        settingsList.add(
            SettingsModel(
                R.drawable.ic_baseline_contact_support_24,
                resources.getString(R.string.support)
            )
        )
        settingsList.add(
            SettingsModel(
                R.drawable.ic_baseline_star_rate_24,
                resources.getString(R.string.rate_us)
            )
        )
        settingsList.add(
            SettingsModel(
                R.drawable.ic_logout,
                resources.getString(R.string.logout)
            )
        )
        settingsAdapter.setList(settingsList)
    }
}