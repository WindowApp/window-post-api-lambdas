package codes.rik.window.api.post.handler

import com.amazonaws.services.lambda.runtime.Context
import java.util.HashMap
import com.amazonaws.services.lambda.runtime.RequestHandler

class SubmitHandler : RequestHandler<Any, Any> {
    override fun handleRequest(input: Any, context: Context): Any {
        val headers = HashMap<String, String>()
        headers.put("Content-Type", "application/json")
        headers.put("X-Custom-Header", "application/json")
        System.err.println("This is a test log message 1")
        System.err.println("This is a test log message 2")
        return GatewayResponse("{ \"Output\": \"Hello World!\"}", headers, 200)
    }
}