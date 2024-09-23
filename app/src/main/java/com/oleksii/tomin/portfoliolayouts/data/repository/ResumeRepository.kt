package com.oleksii.tomin.portfoliolayouts.data.repository

import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Resume

interface ResumeRepository {
    suspend fun getResume(): Resume
}