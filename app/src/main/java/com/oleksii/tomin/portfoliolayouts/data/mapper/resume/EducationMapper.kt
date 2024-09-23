package com.oleksii.tomin.portfoliolayouts.data.mapper.resume

import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.resume.EducationModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Education

class EducationMapper : Mapper<EducationModel, Education> {
    override fun mapToDomain(model: EducationModel): Education {
        requireNotNull(model.institution) { "Institution cant be null" }
        requireNotNull(model.program) { "Program cant be null" }
        requireNotNull(model.from) { "From cant be null" }
        requireNotNull(model.to) { "To cant be null" }
        requireNotNull(model.credentials) { "Credentials cant be null" }

        return Education(
            institution = model.institution,
            program = model.program,
            from = model.from,
            to = model.to,
            credentials = model.credentials,
        )
    }
}