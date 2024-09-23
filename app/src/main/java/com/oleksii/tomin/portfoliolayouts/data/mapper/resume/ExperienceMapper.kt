package com.oleksii.tomin.portfoliolayouts.data.mapper.resume

import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.resume.ExperienceModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Experience

class ExperienceMapper : Mapper<ExperienceModel, Experience> {

    override fun mapToDomain(model: ExperienceModel): Experience {
        requireNotNull(model.company) { "Company cant be null" }
        requireNotNull(model.roles) { "Roles cant be null" }
        requireNotNull(model.from) { "From cant be null" }
        requireNotNull(model.to) { "To cant be null" }
        requireNotNull(model.keyAccomplishments) { "Key accomplishments cant be null" }

        return Experience(
            company = model.company,
            roles = model.roles,
            from = model.from,
            to = model.to,
            keyAccomplishments = model.keyAccomplishments,
        )
    }
}