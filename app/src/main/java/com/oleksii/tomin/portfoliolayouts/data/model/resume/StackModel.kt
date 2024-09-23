package com.oleksii.tomin.portfoliolayouts.data.model.resume

import com.google.gson.annotations.SerializedName

data class StackModel(
    val technologies: List<String>? = null,
    @SerializedName("design-principles")
    val designPrinciples: List<String>? = null
)