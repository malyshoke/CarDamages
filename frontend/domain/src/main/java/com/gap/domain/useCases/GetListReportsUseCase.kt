package com.gap.domain.useCases

import com.gap.domain.ScannerRepository
import com.gap.domain.entities.ReportItem

class GetListReportsUseCase(
    private val repository: ScannerRepository
) {
    operator fun invoke(): List<ReportItem> {
        return repository.getListReports()
    }
}