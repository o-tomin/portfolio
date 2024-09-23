package com.oleksii.tomin.portfoliolayouts.data.model.resume

data class Sys(
    val space: SysLinkModel? = null,
    val id: String? = null,
    val type: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val environment: SysLinkModel? = null,
    val publishedVersion: Int? = null,
    val publishedAt: String? = null,
    val firstPublishedAt: String? = null,
    val createdBy: SysLinkModel? = null,
    val updatedBy: SysLinkModel? = null,
    val publishedCounter: Int? = null,
    val version: Int? = null,
    val publishedBy: SysLinkModel? = null,
    val fieldStatus: FieldStatusModel? = null,
    val automationTags: List<Any>? = null,
    val contentType: SysLinkModel? = null,
    val urn: String? = null
)