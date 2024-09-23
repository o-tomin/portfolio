package com.oleksii.tomin.portfoliolayouts.data.repository

import com.oleksii.tomin.portfoliolayouts.data.datastore.remote.resume.RemoteResumeDetailsSource
import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.resume.ResumeDetailsModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Resume

class ResumeRepositoryImpl(
    val remoteResumeDetailsSource: RemoteResumeDetailsSource,
    val resumeMapper: Mapper<ResumeDetailsModel, Resume>,
) : ResumeRepository {
    override suspend fun getResume(): Resume {
        val fields = remoteResumeDetailsSource.getResume().fields
        requireNotNull(fields) { "Resume Response fields cant be null" }

        val resume = fields.resume
        requireNotNull(resume) { "Resume Model cant be null" }

        return resumeMapper.mapToDomain(resume)
    }
}