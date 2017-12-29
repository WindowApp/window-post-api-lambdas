package codes.rik.window.lambda.common

import codes.rik.window.windowObjectMapper
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import java.io.InputStream

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApiGatewayInput(
        val body: String,
        val headers: Headers?
) {
    companion object {
        fun read(string: String): ApiGatewayInput = objectMapper.readValue(string)
        fun read(input: InputStream): ApiGatewayInput = objectMapper.readValue(input)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Headers(
            @JsonProperty("Content-Type") val contentType: ContentType?,
            @JsonProperty("Accept") val accept: ContentType?)
}

private val objectMapper = windowObjectMapper
