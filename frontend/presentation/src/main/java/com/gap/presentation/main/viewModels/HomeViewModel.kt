package com.gap.presentation.main.viewModels

import androidx.lifecycle.ViewModel
import com.gap.data.ScannerRepositoryImpl
import com.gap.domain.useCases.GetListReportsUseCase

class HomeViewModel: ViewModel() {
    private val repository = ScannerRepositoryImpl
    private val getListReports = GetListReportsUseCase(repository)

    fun isEmptyReportList(): Boolean {
        return getListReports().isEmpty()
    }

}