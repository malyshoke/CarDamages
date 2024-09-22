package com.gap.data

import com.gap.data.network.RetrofitInstance
import com.gap.domain.ScannerRepository
import com.gap.domain.entities.ReportItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object ScannerRepositoryImpl : ScannerRepository {

    private const val TITLE_TAG = "Отчет от 24.05"
    private val scannerApi = RetrofitInstance.api

    private val listReports = mutableListOf<ReportItem>().apply {
        this.add(ReportItem(request_time = "20.05.2024"))
        this.add(ReportItem(request_time = "21.05.2024"))
        this.add(ReportItem(request_time = "21.05.2024"))
        this.add(ReportItem(request_time = "22.05.2024"))
        this.add(ReportItem(request_time = "22.05.2024"))
    }

    override fun getListReports(): List<ReportItem> {
        return listReports
    }

    override suspend fun exchangeFiles(files: List<File>): List<ReportItem> {
        val parts = files.map { file ->
            val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image_files", file.name, requestFile)
        }
        return scannerApi.exchangeFiles(parts)
    }
}
