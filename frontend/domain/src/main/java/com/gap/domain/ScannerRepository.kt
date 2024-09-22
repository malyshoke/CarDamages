package com.gap.domain

import com.gap.domain.entities.ReportItem
import java.io.File

interface ScannerRepository {
    fun getListReports(): List<ReportItem>
    suspend fun exchangeFiles(files: List<File>): List<ReportItem>
}
