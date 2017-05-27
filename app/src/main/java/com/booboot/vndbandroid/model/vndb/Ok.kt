package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Ok : VNDBCommand()
