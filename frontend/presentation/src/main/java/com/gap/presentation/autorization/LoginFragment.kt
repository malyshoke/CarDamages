package com.gap.presentation.autorization

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gap.presentation.R
import com.gap.presentation.autorization.InputCodeFragment.Companion.NUMBER_KEY
import com.gap.presentation.databinding.FragmentLoginBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.ValueListener

class LoginFragment : Fragment() {

    private val navController by lazy { findNavController() }
    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("LoginFragment == null")

    private var state: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhoneSample()
        buttonsWork()
    }

    private fun buttonsWork() {
        binding.ibBack.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_startFragment)
        }
        binding.btnSign.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_inputCodeFragment,
                bundleOf(NUMBER_KEY to binding.etNumber.text?.trim().toString()))
        }
    }

    private fun setupPhoneSample() {


        binding.tilNumber.setEndIconOnClickListener {
            binding.etNumber.setText(getString(R.string.prefix_number))
        }

        val listener = MaskedTextChangedListener(
            getString(R.string.mask_on_number),
            true,
            binding.etNumber,
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {}
            },
            object : ValueListener {

                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String,
                    tailPlaceholder: String
                ) {
                    state = maskFilled
                    updateButtonState()
                }
            })

        binding.etNumber.addTextChangedListener(listener)
        binding.etNumber.onFocusChangeListener = listener

        binding.etNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length > 1) {
                    binding.tilNumber.helperText = getString(R.string.empty_string)
                } else {
                    binding.tilNumber.helperText = getString(R.string.helper_text_number)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }


    private fun updateButtonState() {
        binding.btnSign.isEnabled = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}