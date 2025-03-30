package com.rabbitv.valheimviki.utils

import retrofit2.Response

fun <T> Response<List<T>>.bodyList(): List<T> {
    return body() ?: emptyList()
}