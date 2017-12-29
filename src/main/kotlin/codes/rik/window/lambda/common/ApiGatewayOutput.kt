package codes.rik.window.lambda.common

import java.util.*

data class ApiGatewayOutput(
        val body: String,
        val statusCode: Int,
        val isBase64Encoded: Boolean,
        val headers: Map<String, String>) {

    companion object {
        fun create(output: OutputBody, statusCode: Int = 200, headers: Map<String, String>  = mapOf()) =
            ApiGatewayOutput(
                    body = output.body,
                    statusCode = statusCode,
                    isBase64Encoded = output.isBase64Encoded,
                    headers = headers)
    }
}

sealed class OutputBody(val body: String, val isBase64Encoded: Boolean)
class StringOutputBody(string: String): OutputBody(string, isBase64Encoded = false)
class BinaryOutputBody(bytes: ByteArray): OutputBody(Base64.getEncoder().encodeToString(bytes), isBase64Encoded = true)
