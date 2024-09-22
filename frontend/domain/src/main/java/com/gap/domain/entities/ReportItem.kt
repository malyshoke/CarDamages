package com.gap.domain.entities

import java.io.Serializable

data class ReportItem(
    val labels: Labels = Labels(),
    val part: String = "",
    val request_time: String = "",
    val urls: String = ""
) : Serializable
