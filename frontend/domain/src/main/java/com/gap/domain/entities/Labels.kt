package com.gap.domain.entities

import java.io.Serializable

data class Labels(
    val background: Int = 0,
    val dent: Int = 0,
    val glass_shatter: Int = 0,
    val lamp_broken: Int = 0,
    val scratch: Int = 0,
    val tire_flat: Int = 0
) : Serializable {
    fun getDescription(): String {
        if (background == 1) return "Повреждения не найдены"

        val detectedDamage = listOf(
            "царапины" to scratch,
            "вмятины" to dent,
            "повреждение стекла" to glass_shatter,
            "повреждение фары" to lamp_broken,
            "спущенные шины" to tire_flat
        ).filter { it.second > 0 }
            .joinToString { "${it.first} ${it.second}" }

        return if (detectedDamage.isNotEmpty()) "Обнаружено: $detectedDamage" else "Повреждения не найдены"
    }
}


