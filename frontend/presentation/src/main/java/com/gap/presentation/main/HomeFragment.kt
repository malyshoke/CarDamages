package com.gap.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gap.presentation.R
import com.gap.presentation.autorization.InputCodeFragment
import com.gap.presentation.databinding.FragmentHomeBinding
import com.gap.presentation.databinding.FragmentInputCodeBinding
import com.gap.presentation.main.adapters.HomeAdapter
import com.gap.presentation.main.viewModels.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var adapter: HomeAdapter
    private val navController by lazy { findNavController() }
    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("HomeFragment == null")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveStartFragment(true)
        workWithRV()
        workWithButtons()
        workWithUI()
    }

    private fun workWithUI() {

    }

    private fun workWithButtons() {
        with(binding) {
            ivLogout.setOnClickListener {
                saveStartFragment(false)
                navController.navigate(R.id.action_homeFragment_to_startFragment)
            }
            btnCreateReport.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_startReportFragment)
            }
        }
    }

    private fun workWithRV() {
        adapter = HomeAdapter()
        binding.recyclerView.adapter = adapter
        if (viewModel.isEmptyReportList()) {
            binding.recyclerView.visibility = View.GONE
            binding.tvEmptyRv.visibility = View.VISIBLE
            binding.ivArrowDown.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvEmptyRv.visibility = View.GONE
            binding.ivArrowDown.visibility = View.GONE
        }
    }

    private fun saveStartFragment(isHomeScreenFragmentStart: Boolean) {
        val sharedPreferences = requireActivity()
            .getSharedPreferences(START_FRAGMENT_TAG, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(REACHED_HOME_FRAGMENT, isHomeScreenFragmentStart)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val START_FRAGMENT_TAG = "start_fragment_tag"
        const val REACHED_HOME_FRAGMENT = "reachedHomeFragment"
    }
}