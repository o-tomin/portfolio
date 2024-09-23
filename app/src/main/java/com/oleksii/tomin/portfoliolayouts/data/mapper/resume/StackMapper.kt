package com.oleksii.tomin.portfoliolayouts.data.mapper.resume

import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.resume.StackModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Stack

class StackMapper : Mapper<StackModel, Stack> {

    override fun mapToDomain(model: StackModel): Stack {
        requireNotNull(model.technologies) { "Technologies cant be null" }
        requireNotNull(model.designPrinciples) { "Design principles cant be null" }

        return Stack(
            technologies = model.technologies,
            designPrinciples = model.designPrinciples,
        )
    }
}