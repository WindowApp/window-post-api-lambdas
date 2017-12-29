import codes.rik.window.lambda.common.ApiGatewayInput
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object SubmitHandlerTest {

    @JvmStatic
    fun main(args: Array<String>) {

        System.err.println("1")
        val input: ApiGatewayInput = jacksonObjectMapper().readValue("")
        System.err.println("2")

    }

}