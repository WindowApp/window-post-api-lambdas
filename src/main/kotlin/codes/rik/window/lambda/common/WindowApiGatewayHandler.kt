package codes.rik.window.lambda.common

import codes.rik.window.lambda.common.ContentType.JSON
import codes.rik.window.lambda.common.ContentType.PROTOBUF
import codes.rik.window.util.gson
import com.github.salomonbrys.kotson.typedToJson
import com.google.gson.stream.JsonWriter
import com.google.protobuf.AbstractMessage
import com.google.protobuf.Message
import mu.KotlinLogging
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.util.*
import kotlin.text.Charsets.UTF_8

private val logger = KotlinLogging.logger {}
abstract class WindowApiGatewayHandler<in I: AbstractMessage, out O: AbstractMessage>(
        private val prototype: I
): WindowLambdaHandler<ApiGatewayInput>(ApiGatewayInput.Companion::read) {

    override fun handle(input: ApiGatewayInput, output: OutputStream) {
        // Parse the actual request (dependent on content type)
        val request: I = parseRequestBody(input)
        logger.debug { "Handling parsed request: $request" }

        // Actually handle the request
        handle0(request) { response ->
            // Got a response
            logger.info { "Response: $response" }

            // Encode a response body
            val apiOutput = ApiGatewayOutput.create(
                    headers = mapOf("X-Powered-By" to "Window"),
                    output = encodeResponseBody(input, response))
            logger.debug { "Output: $apiOutput" }

            JsonWriter(OutputStreamWriter(output, UTF_8)).use {
                gson.typedToJson(apiOutput, it)
            }
        }
    }

    abstract fun handle0(input: I, callback: (O) -> Unit)

    @Suppress("UNCHECKED_CAST")
    private fun parseRequestBody(input: ApiGatewayInput): I = when(input.headers?.contentType ?: PROTOBUF) {
        PROTOBUF -> prototype.parserForType.parseFrom(Base64.getDecoder().decode(input.body)) as I
        JSON -> prototype.newFromJson(input.body) as I
    }

    private fun encodeResponseBody(input: ApiGatewayInput, response: O) = when(input.headers?.accept ?: PROTOBUF) {
        PROTOBUF -> BinaryOutputBody(response.toByteArray())
        JSON -> StringOutputBody(com.google.protobuf.util.JsonFormat.printer().print(response))
    }
}

private fun AbstractMessage.newFromJson(json: String): Message {
    val builder = this.newBuilderForType()
    com.google.protobuf.util.JsonFormat.parser().merge(json, builder)
    return builder.build()
}
