package com.gap.presentation.autorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gap.presentation.R
import com.gap.presentation.databinding.FragmentStartBinding


class StartFragment : Fragment() {

    private val navController by lazy { findNavController() }
    private var _binding: FragmentStartBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("StartFragment == null")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSign.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_loginFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}