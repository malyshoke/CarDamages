package com.gap.presentation.autorization

import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gap.presentation.R
import com.gap.presentation.databinding.FragmentInputCodeBinding
import java.util.concurrent.TimeUnit


class InputCodeFragment : Fragment() {
    private val navController by lazy { findNavController() }
    private var _binding: FragmentInputCodeBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("InputCodeFragment == null")
    private lateinit var args: String
    private var currentFocusedEditText: View? = null
    private val editTextFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        currentFocusedEditText = if (hasFocus) v else null
    }
    private var currentNightMode = 0
    private var timer: CountDownTimer? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputCodeBinding.inflate(inflater, container, false)
        args = arguments?.getString(NUMBER_KEY) ?: ""
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: $args")
        buttonsWork()
        raiseTheKeyboard()
        inputInString()
        whereIsFocus()
        setupListeners()
        bindingFocus()
        setupUi()
        workWithTimer()
    }

    private fun setupUi() {
        currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.linearLayout.setBackgroundResource(android.R.color.transparent)
        binding.viewForeground.setBackgroundResource(android.R.color.transparent)
    }

    private fun whereIsFocus() {
        with(binding) {
            viewForeground.setOnClickListener {
                edT1.onFocusChangeListener = editTextFocusChangeListener
                edT2.onFocusChangeListener = editTextFocusChangeListener
                edT3.onFocusChangeListener = editTextFocusChangeListener
                edT4.onFocusChangeListener = editTextFocusChangeListener
                if (currentFocusedEditText == null) {
                    raiseTheKeyboard()
                }
                if (readTheCode().length == 4) {
                    binding.edT4.requestFocus()
                }
                showKeyboardForEditText(setFocusToET())
                Log.d(TAG, "whereIsFocus: $currentFocusedEditText")
            }
        }

    }

    private fun inputInString() {
        val args = args//.number
        val ourText: String = resources.getString(R.string.text_for_code_input)
        val formattedText = String.format(ourText, args)
        val spannableText = SpannableString(formattedText)
        val startIndex = formattedText.indexOf(args)
        val endIndex = startIndex + args.length
        spannableText.setSpan(
            StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewInputNumber.text = spannableText
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun raiseTheKeyboard() {
        binding.edT1.requestFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.edT1, InputMethodManager.SHOW_IMPLICIT)
        listOf(binding.edT1, binding.edT2, binding.edT3, binding.edT4).forEach {
            it.tooltipText = null
        }
    }

    private fun buttonsWork() {
        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setFocusToET(): View? {
        return when (currentFocusedEditText) {
            binding.edT1 -> binding.edT1.also {
                it.requestFocus()
            }

            binding.edT2 -> binding.edT2.also {
                it.requestFocus()
            }

            binding.edT3 -> binding.edT3.also {
                it.requestFocus()
            }

            binding.edT4 -> binding.edT4.also {
                it.requestFocus()
            }

            else -> {
                return null
            }
        }
    }

    private fun showKeyboardForEditText(editText: View?) {
        editText?.let {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun workWithTimer() {

        startCountdownTimer()
        binding.textViewTimer.setOnClickListener {
            if (isSendCodeAgainVisible()) {
                startCountdownTimer()

            }
        }
    }

    private fun isSendCodeAgainVisible(): Boolean {
        return binding.textViewTimer.text == getString(R.string.send_code_again)
    }

    private fun startCountdownTimer() {
        // Determine text color based on theme
        val textColorResId =
            if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
                // Light theme
                R.color.black
            } else {
                // Dark theme
                R.color.white
            }
        timer?.cancel()

        val timerDurationInMillis: Long = TimeUnit.SECONDS.toMillis(59)
        timer = object : CountDownTimer(timerDurationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the timer text on each tick
                val secondsLeft = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                val timerText = getString(R.string.input_code_timer, secondsLeft)
                val spannableText = SpannableString(timerText)



                spannableText.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), textColorResId)),
                    timerText.indexOf(secondsLeft.toString()),
                    timerText.indexOf(secondsLeft.toString()) + secondsLeft.toString().length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.textViewTimer.text = spannableText
            }

            override fun onFinish() {
                // Timer finished, update the text and color accordingly
                binding.textViewTimer.text = getString(R.string.send_code_again)
                binding.textViewTimer.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        textColorResId
                    )
                )
            }
        }.start()
    }


    private fun bindingFocus() {
        binding.edT1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                    // Текущая тема - светлая
                    binding.edT1.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    // Текущая тема - темная
                }

            } else {
                if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                    // Текущая тема - светлая
                    binding.edT1.setBackgroundResource(R.drawable.round_edit_text)
                } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    // Текущая тема - темная
                }
            }
        }

    }


    private fun setupListeners() {
        with(binding) {
            edT1.addTextChangedListener(CustomTextWatcher(object : OnTextChangedListener {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        binding.edT1.setSelection(1)
                        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                            // Текущая тема - светлая
                            binding.edT2.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                            binding.edT1.setBackgroundResource(R.drawable.round_edit_text)
                        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            // Текущая тема - темная
                        }
                    }
                    if (s?.length == 2) {
                        val input = s.toString()
                        val firstNumber = input.substring(0, 1)
                        binding.edT1.setText(firstNumber)
                        binding.edT1.setSelection(1) // Устанавливаем курсор после первого символа
                        binding.edT2.requestFocus()
                        binding.edT2.setText(input.substring(1))
                    }
                    if (s?.length == 0 && binding.edT2.length() == 0 && binding.edT3.length() == 0 && binding.edT4.length() == 0) {
                        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                            // Текущая тема - светлая
                            binding.edT2.setBackgroundResource(R.drawable.round_edit_text)
                            binding.edT1.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            // Текущая тема - темная
                        }

                    }
                }
            }))
            edT2.addTextChangedListener(CustomTextWatcher(object : OnTextChangedListener {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        binding.edT2.setSelection(1)
                        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                            // Текущая тема - светлая
                            binding.edT3.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                            binding.edT2.setBackgroundResource(R.drawable.round_edit_text)
                        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            // Текущая тема - темная
                        }

                    }
                    if (s?.length == 2) {
                        val input = s.toString()
                        val firstNumber = s.toString().substring(0, 1)
                        binding.edT2.setText(firstNumber)
                        binding.edT2.setSelection(1) // Устанавливаем курсор после первого символа
                        binding.edT3.requestFocus()
                        binding.edT3.setText(input.substring(1))
                    }
                    if (s?.length == 0) {
                        binding.edT1.requestFocus()
                    }
                    if (s?.length == 0 && binding.edT3.length() == 0 && binding.edT4.length() == 0) {
                        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                            // Текущая тема - светлая
                            binding.edT3.setBackgroundResource(R.drawable.round_edit_text)
                            binding.edT2.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                            binding.edT1.setBackgroundResource(R.drawable.round_edit_text)
                        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            // Текущая тема - темная
                        }

                    }

                }
            }))
            edT3.addTextChangedListener(CustomTextWatcher(object : OnTextChangedListener {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (s?.length == 1) {
                        binding.edT3.setSelection(1)
                        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                            // Текущая тема - светлая
                            binding.edT4.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                            binding.edT3.setBackgroundResource(R.drawable.round_edit_text)
                        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            // Текущая тема - темная
                        }

                    }
                    if (s?.length == 2) {
                        val input = s.toString()
                        val firstNumber = s.toString().substring(0, 1)
                        binding.edT3.setText(firstNumber)
                        binding.edT3.setSelection(1) // Устанавливаем курсор после первого символа
                        binding.edT4.requestFocus()
                        binding.edT4.setText(input.substring(1))
                    }
                    if (s?.length == 0) {
                        binding.edT2.requestFocus()
                    }
                    if (s?.length == 0 && binding.edT4.length() == 0) {
                        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                            // Текущая тема - светлая
                            binding.edT4.setBackgroundResource(R.drawable.round_edit_text)
                            binding.edT3.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            // Текущая тема - темная
                        }

                    }
                }
            }))
            edT4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    binding.errorTV.text = ""
                    errorViewCode(false, DEFAULT)
//                buttonOff()
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    validation()

                    if (s?.length == 1) {
                        binding.edT4.setSelection(1)
                    }
                    if (s?.length == 0) {
                        binding.edT3.requestFocus()
                    }
                    if (s?.length == 0 && binding.edT3.text?.length == 1) {//
                        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                            // Текущая тема - светлая
                            binding.edT4.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                        } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            // Текущая тема - темная
                        }

                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }


    private fun validation() {
        with(binding) {
            if (edT4.checkState()) {
                if (edT1.checkState() &&
                    edT2.checkState() &&
                    edT3.checkState() &&
                    edT4.checkState()
                ) {
                    if (edT1.text.toString() +
                        edT2.text.toString() +
                        edT3.text.toString() +
                        edT4.text.toString() == "5492"
                    ) {
                        errorViewCode(true, DEFAULT_DOP)
                        launchFragment()
                    } else {
                        errorViewCode(false, DEFAULT_DOP)
                    }
                } else {
                    binding.errorTV.text = "Поля не могут быть пустыми"
                }
            }
        }
    }

    private fun EditText.checkState(): Boolean {
        return this.text.toString().trim().isNotEmpty()
    }


    private fun readTheCode(): String {
        return binding.edT1.text.toString().trim() + binding.edT2.text.toString()
            .trim() + binding.edT3.text.toString().trim() + binding.edT4.text.toString().trim()
    }


    private fun launchFragment() {
        navController.navigate(R.id.action_inputCodeFragment_to_homeFragment)
    }

    private fun errorViewCode(isValid: Boolean, default: Int) {

        with(binding) {

            val listCode = listOf(edT1, edT2, edT3, edT4)

            if (isValid) {
                listCode.forEach {
                    if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                        // Текущая тема - светлая
                        it.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
                        it.setBackgroundResource(R.drawable.round_edit_text_dedicated)
                    } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                        // Текущая тема - темная
                    }


                }
            } else {
                for (i in listCode) {

                    if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                        // Текущая тема - светлая
                        i.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                        i.setBackgroundResource(R.drawable.round_edit_text_error)
                    } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                        // Текущая тема - темная
                    }


                }
            }
            if (default == 0) {
                for (i in listCode) {


                    if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                        // Текущая тема - светлая
                        i.setBackgroundResource(R.drawable.round_edit_text)
                        i.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    } else if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                        // Текущая тема - темная
                        i.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }


                }
            }
        }
    }


    interface OnTextChangedListener {
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
    }

    inner class CustomTextWatcher(private val listener: OnTextChangedListener) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {}
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        timer?.cancel()
    }

    companion object {
        const val NUMBER_KEY = "number"
        private const val TAG = "InputCodeFragmentLog"
        private const val DEFAULT = 0
        private const val DEFAULT_DOP = 1
    }

}