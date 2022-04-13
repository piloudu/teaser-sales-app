package com.example.sample_sales_app.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

internal val mapper = jacksonObjectMapper()
internal fun <T> String.deserialize(): List<T> = mapper.readValue(this)
