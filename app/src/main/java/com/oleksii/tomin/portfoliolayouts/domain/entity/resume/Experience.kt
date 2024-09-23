package com.oleksii.tomin.portfoliolayouts.domain.entity.resume

data class Experience(
    val company: String,
    val roles: List<String>,
    val from: String,
    val to: String,
    val keyAccomplishments: List<String>
)