package codes.rik.window.lambda.common

import com.fasterxml.jackson.annotation.JsonCreator

enum class ContentType(val httpContentType: String) {
    JSON("application/json"),
    PROTOBUF("application/protobuf");

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromStringContentType(string: String): ContentType? = values().firstOrNull { it.httpContentType == string }
    }
}
