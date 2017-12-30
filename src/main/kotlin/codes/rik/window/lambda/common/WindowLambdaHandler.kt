package codes.rik.window.lambda.common

import codes.rik.window.post.environment.Environment
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.google.common.io.CharStreams
import mu.KotlinLogging
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream

private val logger = KotlinLogging.logger {}

abstract class WindowLambdaHandler<in T>(private val parser: (String) -> T): RequestStreamHandler {
    protected val environment = Environment.current

    override fun handleRequest(input: InputStream, output: OutputStream, context: Context) {
        // Receive input string
        val inputString = CharStreams.toString(InputStreamReader(input, Charsets.UTF_8))
        logger.debug { "Received input: $inputString" }

        // Parse the input
        val inputContainer = parser(inputString)
        logger.info { "Parsed input: $inputContainer" }

        // Actually handle
        handle(inputContainer, output)
    }

    abstract fun handle(input: T, output: OutputStream)
}
