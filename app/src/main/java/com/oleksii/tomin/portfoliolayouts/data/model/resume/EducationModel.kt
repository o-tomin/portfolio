package com.oleksii.tomin.portfoliolayouts.data.model.resume

data class EducationModel(
    val institution: String? = null,
    val program: String? = null,
    val from: String? = null,
    val to: String? = null,
    val credentials: List<String>? = null
)