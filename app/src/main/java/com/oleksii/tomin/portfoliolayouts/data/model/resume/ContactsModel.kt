package com.oleksii.tomin.portfoliolayouts.data.model.resume

import com.google.gson.annotations.SerializedName

data class ContactsModel(
    val name: String? = null,
    val title: String? = null,
    val location: String? = null,
    val email: String? = null,
    val linkedin: String? = null,
    @SerializedName("linkedin-view-profile-url")
    val linkedinViewProfileUrl: String? = null,
    val phone: String? = null,
)