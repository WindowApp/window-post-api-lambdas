package codes.rik.window.lambda.common

import codes.rik.window.gson
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.annotations.SerializedName

data class ApiGatewayInput(
        val body: String,
        val headers: Headers?
) {
    companion object {
        fun read(string: String): ApiGatewayInput = gson.fromJson(string)
    }

    data class Headers(
            @SerializedName("Content-Type") val contentType: ContentType?,
            @SerializedName("Accept") val accept: ContentType?)
}
