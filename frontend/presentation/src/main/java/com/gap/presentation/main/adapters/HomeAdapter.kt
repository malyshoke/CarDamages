package com.gap.presentation.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.gap.data.ScannerRepositoryImpl
import com.gap.domain.useCases.GetListReportsUseCase
import com.gap.presentation.R
import com.gap.presentation.databinding.ItemReportBinding

class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {
    private val repository = ScannerRepositoryImpl
    private val getListReports = GetListReportsUseCase(repository)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: HomeViewHolder, position: Int) {
        val reportsList = getListReports.invoke()
        val report = reportsList[position]
        viewHolder.binding.tvDate.text = report.request_time
        onItemClickListener(viewHolder)
    }

    private fun onItemClickListener(viewHolder: HomeViewHolder) {
        viewHolder.itemView.setOnClickListener {
            (viewHolder.itemView.context as? AppCompatActivity)?.let { activity ->
                val navHostFragment =
                    activity.supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_homeFragment_to_reportFragment)
            }
        }
    }

    override fun getItemCount(): Int = getListReports.invoke().size
}