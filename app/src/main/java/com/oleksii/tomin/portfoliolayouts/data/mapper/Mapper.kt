package com.oleksii.tomin.portfoliolayouts.data.mapper

interface Mapper<in T, out U> {
    fun mapToDomain(model: T): U
}