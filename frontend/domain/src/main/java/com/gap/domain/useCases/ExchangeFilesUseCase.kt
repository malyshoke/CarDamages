package com.gap.domain.useCases

import com.gap.domain.ScannerRepository
import com.gap.domain.entities.ReportItem
import java.io.File

class ExchangeFilesUseCase(
    private val repository: ScannerRepository
) {
    suspend operator fun invoke(files: List<File>): List<ReportItem> {
        return repository.exchangeFiles(files)
    }
}