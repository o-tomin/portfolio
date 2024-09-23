package com.oleksii.tomin.portfoliolayouts.data.model.resume

data class ResumeDetailsModel(
    val contacts: ContactsModel? = null,
    val github: String? = null,
    val summary: String? = null,
    val stack: StackModel? = null,
    val experience: List<ExperienceModel>? = null,
    val education: List<EducationModel>? = null
)