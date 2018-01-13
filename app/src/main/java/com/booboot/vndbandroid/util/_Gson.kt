package com.booboot.vndbandroid.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.lang.reflect.Type

inline fun <reified T> type(): Type = object : TypeToken<T>() {}.type

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, type<T>())

inline fun <reified T> Gson.fromJson(reader: JsonReader) = this.fromJson<T>(reader, type<T>())