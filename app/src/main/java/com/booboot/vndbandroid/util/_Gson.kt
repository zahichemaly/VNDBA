package com.booboot.vndbandroid.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

inline fun <reified T> type(): TypeToken<T> = object : TypeToken<T>() {}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, type<T>().type)

inline fun <reified T> Gson.fromJson(reader: JsonReader) = this.fromJson<T>(reader, type<T>().type)