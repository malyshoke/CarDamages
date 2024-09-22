package com.gap.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gap.domain.entities.ReportItem
import com.gap.presentation.R
import com.gap.presentation.databinding.FragmentHomeBinding
import com.gap.presentation.databinding.FragmentReportBinding
import com.gap.presentation.main.viewModels.HomeViewModel


class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("ReportFragment == null")
    private val navController by lazy { findNavController() }
    private lateinit var reportItems: List<ReportItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reportItems = arguments?.getSerializable(REPORT_ITEM_KEY) as? ArrayList<ReportItem>
        reportItems?.let {
            this.reportItems = it.toList()
        }
        workWithUi()
        workWithButtons()
    }

    private fun workWithUi() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvTitleDate.text = reportItems[0].request_time
        reportItems.forEach { item ->
            when (item.part) {
                "front" -> {
                    Glide.with(this).load(item.urls).into(binding.ivFrontPart)
                    binding.tvFrontDescription.text = item.labels.getDescription()
                }

                "left" -> {
                    Glide.with(this).load(item.urls).into(binding.ivLeftPart)
                    binding.tvLeftDescription.text = item.labels.getDescription()
                }

                "back" -> {
                    Glide.with(this).load(item.urls).into(binding.ivBackPart)
                    binding.tvBackDescription.text = item.labels.getDescription()
                }

                "right" -> {
                    Glide.with(this).load(item.urls).into(binding.ivRightPart)
                    binding.tvRightDescription.text = item.labels.getDescription()
                }
            }
        }
        binding.progressBar.visibility = View.INVISIBLE
    }


    private fun workWithButtons() {
        with(binding) {
            ibBack.setOnClickListener {
                navController.navigate(R.id.action_reportFragment_to_sendPhotosFragment)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val REPORT_ITEM_KEY = "reportItemKey"
    }

}