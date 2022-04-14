package com.example.sample_sales_app.get_data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

private val mapper = jacksonObjectMapper()
fun <T> String.deserialize(): List<T> = mapper.readValue(this)