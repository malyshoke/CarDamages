package com.gap.data.network

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import com.gap.domain.entities.ReportItem

interface ScannerApi {
    @Multipart
    @POST("/return4Files")
    suspend fun exchangeFiles(@Part files: List<MultipartBody.Part>): List<ReportItem>
}
