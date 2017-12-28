package codes.rik.window.lambda.common

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.io.CharStreams
import com.google.protobuf.AbstractMessage
import com.google.protobuf.Message
import mu.KLogging
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.*

abstract class WindowApiGatewayHandler<in I: AbstractMessage, out O: AbstractMessage>(
        private val prototype: I
) : RequestStreamHandler {
    companion object: KLogging()

    override fun handleRequest(input: InputStream, output: OutputStream, context: Context) {
        // Receive input string
        val inputString = CharStreams.toString(InputStreamReader(input, Charsets.UTF_8))
        logger.info { "Received input: $inputString" }

        // Parse the API gateway input (JSON)
        val inputContainer = ApiGatewayInput.read(inputString)
        logger.info { "Parsed input: $inputContainer" }

        // Parse the actual request (dependent on content type)
        val request: I = parseRequestBody(inputContainer)
        logger.info { "Handling parsed request: $request" }

        // Actually handle the request
        handle(request, { response ->
            // Got a response
            logger.info { "Response: $response" }

            // Encode a response body
            val apiOutput = ApiGatewayOutput(
                    headers = mapOf("X-Powered-By" to "WordFart"),
                    output = encodeResponseBody(inputContainer, response))

            logger.info { "Encoded output: $apiOutput" }

            objectMapper.writeValue(output, apiOutput)
        })
    }

    abstract fun handle(input: I, callback: (O) -> Unit)

    @Suppress("UNCHECKED_CAST")
    private fun parseRequestBody(input: ApiGatewayInput): I = when(input.headers?.contentType ?: ContentType.PROTOBUF) {
        ContentType.PROTOBUF -> prototype.parserForType.parseFrom(Base64.getDecoder().decode(input.body)) as I
        ContentType.JSON -> prototype.newFromJson(input.body) as I
    }

    private fun encodeResponseBody(input: ApiGatewayInput, response: O) = when(input.headers?.accept ?: ContentType.PROTOBUF) {
        ContentType.PROTOBUF -> BinaryOutputBody(response.toByteArray())
        ContentType.JSON -> StringOutputBody(com.google.protobuf.util.JsonFormat.printer().print(response))
    }
}

private val objectMapper = jacksonObjectMapper()

private fun AbstractMessage.newFromJson(json: String): Message {
    val builder = this.newBuilderForType()
    com.google.protobuf.util.JsonFormat.parser().merge(json, builder)
    return builder.build()
}
