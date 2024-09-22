package com.gap.presentation.main.createReport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gap.presentation.R
import com.gap.presentation.databinding.FragmentStartReportBinding


class StartReportFragment : Fragment() {

    private var _binding: FragmentStartReportBinding? = null
    private val binding: FragmentStartReportBinding
        get() = _binding ?: throw RuntimeException("WelcomeFragment == null")
    private val navController by lazy { findNavController() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workWithUI()
    }

    private fun workWithUI() {
        with(binding) {
            ibBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            btnWelcome.setOnClickListener {
                navController.navigate(R.id.action_startReportFragment_to_sendPhotosFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "WelcomeFragmentLog"
    }
}