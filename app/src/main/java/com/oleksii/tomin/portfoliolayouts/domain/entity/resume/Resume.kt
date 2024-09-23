package com.oleksii.tomin.portfoliolayouts.domain.entity.resume

class Resume(
    val contacts: Contacts,
    val github: String,
    val summary: String,
    val stack: Stack,
    val experience: List<Experience>,
    val education: List<Education>
)