import com.google.gson.annotations.SerializedName

data class ResumeResponse(
    val metadata: Metadata,
    val sys: Sys,
    val fields: Fields
)

data class Metadata(
    val tags: List<Any>,
    val concepts: List<Any>
)

data class Sys(
    val space: SysLink,
    val id: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val environment: SysLink,
    val publishedVersion: Int,
    val publishedAt: String,
    val firstPublishedAt: String,
    val createdBy: SysLink,
    val updatedBy: SysLink,
    val publishedCounter: Int,
    val version: Int,
    val publishedBy: SysLink,
    val fieldStatus: FieldStatus,
    val automationTags: List<Any>,
    val contentType: SysLink,
    val urn: String
)

data class SysLink(
    val sys: LinkDetails
)

data class LinkDetails(
    val type: String,
    val linkType: String,
    val id: String
)

data class FieldStatus(
    val enUS: String
)

data class Fields(
    val resume: ResumeDetails
)

data class ResumeDetails(
    val contact: Contact,
    val github: String,
    val summary: String,
    val stack: Stack,
    val experience: List<Experience>,
    val education: List<Education>
)

data class Contact(
    val name: String,
    val title: String,
    val location: String,
    val email: String,
    val linkedin: String,
    @SerializedName("linkedin-view-profile-url")
    val linkedinViewProfileUrl: String,
    val phone: String,
    val formattedPhoneContact: String = phone
)

data class Stack(
    val technologies: List<String>,
    val designPrinciples: List<String>
)

data class Experience(
    val company: String,
    val roles: List<String>,
    val from: String,
    val to: String,
    val keyAccomplishments: List<String>
)

data class Education(
    val institution: String,
    val program: String,
    val from: String,
    val to: String,
    val credentials: List<String>
)