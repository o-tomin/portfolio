package com.oleksii.tomin.portfoliolayouts.domain.entity.resume

data class Contacts(
    val name: String,
    val title: String,
    val location: String,
    val email: String,
    val linkedin: String,
    val linkedinViewProfileUrl: String,
    val phone: String,
    val formattedPhoneContact: String,
)