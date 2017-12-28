package codes.rik.window.lambda.common

import java.util.*

data class ApiGatewayOutput(
        val statusCode: Int = 200,
        private val output: OutputBody,
        val headers: Map<String, String> = mapOf()) {

    val body get() = output.body
    val isIsBase64Encoded get() = output.isBase64Encoded // API Gateway expects the property to be called "isBase64Encoded" => isIs
}

abstract class OutputBody(val body: String, val isBase64Encoded: Boolean)
data class StringOutputBody(val string: String): OutputBody(string, isBase64Encoded = false)
data class BinaryOutputBody(val bytes: ByteArray): OutputBody(Base64.getEncoder().encodeToString(bytes), isBase64Encoded = true)
