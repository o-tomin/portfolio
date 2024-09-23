package com.oleksii.tomin.portfoliolayouts.data.mapper.resume

import com.oleksii.tomin.portfoliolayouts.data.mapper.Mapper
import com.oleksii.tomin.portfoliolayouts.data.model.resume.ContactsModel
import com.oleksii.tomin.portfoliolayouts.domain.entity.resume.Contacts

class ContactsMapper : Mapper<ContactsModel, Contacts> {

    override fun mapToDomain(model: ContactsModel): Contacts {
        requireNotNull(model.name) { "Name cant be null" }
        requireNotNull(model.email) { "Email cant be null" }
        requireNotNull(model.phone) { "Phone cant be null" }
        requireNotNull(model.title) { "Title cant be null" }
        requireNotNull(model.linkedin) { "LenkedIn cant be null" }
        requireNotNull(model.linkedinViewProfileUrl) { "LinkedIn public profile url cant be null" }
        requireNotNull(model.location) { "Location cant be null" }

        return Contacts(
            name = model.name,
            email = model.email,
            phone = model.phone,
            title = model.title,
            linkedin = model.linkedin,
            location = model.location,
            linkedinViewProfileUrl = model.linkedinViewProfileUrl,
            formattedPhoneContact = formatPhone(model.phone)
        )
    }

    private fun formatPhone(phone: String): String {
        if (!phone.startsWith("+") || phone.length != 12) {
            return phone
        }

        val countryCode = phone.substring(0, 2)
        val areaCode = phone.substring(2, 5)
        val firstPart = phone.substring(5, 8)
        val secondPart = phone.substring(8, 12)

        return "$countryCode ($areaCode) $firstPart $secondPart"
    }
}
