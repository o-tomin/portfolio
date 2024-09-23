package com.oleksii.tomin.portfoliolayouts.data.mapper.resume

import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.resume.ContactsModel
import com.oleksii.tomin.portfoliolayouts.data.model.resume.EducationModel
import com.oleksii.tomin.portfoliolayouts.data.model.resume.ExperienceModel
import com.oleksii.tomin.portfoliolayouts.data.model.resume.ResumeDetailsModel
import com.oleksii.tomin.portfoliolayouts.data.model.resume.StackModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Contacts
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Education
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Experience
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Resume
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Stack

class ResumeMapper(
    private val contactsMapper: Mapper<ContactsModel, Contacts>,
    private val stackMapper: Mapper<StackModel, Stack>,
    private val experienceMapper: Mapper<ExperienceModel, Experience>,
    private val educationMapper: Mapper<EducationModel, Education>,
) : Mapper<ResumeDetailsModel, Resume> {

    override fun mapToDomain(model: ResumeDetailsModel): Resume {
        requireNotNull(model.github) { "GitHub link cant be null" }
        requireNotNull(model.stack) { "Stack cant be null" }
        requireNotNull(model.contacts) { "Contacts cant be null" }
        requireNotNull(model.summary) { "Summary cant be null" }
        requireNotNull(model.education) { "Education cant be null" }
        requireNotNull(model.experience) { "Experience cant be null" }

        return Resume(
            contacts = model.contacts.let(contactsMapper::mapToDomain),
            github = model.github,
            summary = model.summary,
            stack = model.stack.let(stackMapper::mapToDomain),
            experience = model.experience.map(experienceMapper::mapToDomain),
            education = model.education.map(educationMapper::mapToDomain),
        )
    }

}