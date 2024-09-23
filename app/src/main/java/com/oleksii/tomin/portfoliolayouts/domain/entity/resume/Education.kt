package com.oleksii.tomin.portfoliolayouts.domain.entity.resume

data class Education(
    val institution: String,
    val program: String,
    val from: String,
    val to: String,
    val credentials: List<String>
)