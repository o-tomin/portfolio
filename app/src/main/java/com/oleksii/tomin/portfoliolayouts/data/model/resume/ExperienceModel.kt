package com.oleksii.tomin.portfoliolayouts.data.model.resume

import com.google.gson.annotations.SerializedName

class ExperienceModel(
    val company: String? = null,
    val roles: List<String>? = null,
    val from: String? = null,
    val to: String? = null,
    @SerializedName("key-accomplishments")
    val keyAccomplishments: List<String>? = null
)