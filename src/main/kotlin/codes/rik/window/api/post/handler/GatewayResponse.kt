package codes.rik.window.api.post.handler

import java.util.*
import java.util.Collections.unmodifiableMap


/**
 * POJO containing response object for API Gateway.
 */
class GatewayResponse(val body: String, val headers: Map<String, String>, val statusCode: Int)
