package com.it.template.api.model

class AppVersion(
    val version: String
) {
    override fun toString(): String {
        return "{ \"version\": \"$version\" }"
    }
}
