import codes.rik.window.lambda.common.ApiGatewayInput

object SubmitHandlerTest {

    @JvmStatic
    fun main(args: Array<String>) {

        val s = """{"httpMethod":"POST","body":"hello","resource":"/submit","requestContext":{"resourcePath":"/submit",
            |"httpMethod":"POST","stage":"prod","identity":{"sourceIp":"127.0.0.1:54752"}},"queryStringParameters":{},"headers":{"Accept":"application/json","Accept-Encoding":"gzip, deflate, br","Accept-Language":"en-GB,en;q=0.9,en-US;q=0.8","Cache-Control":"no-cache","Connection":"keep-alive","Content-Length":"2","Content-Type":"application/json","Dnt":"1","Origin":"chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop","Postman-Token":"e8937dec-30a1-f5e8-0ec7-60db476627d6","User-Agent":"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36"},"pathParameters":null,"stageVariables":null,"path":"/submit"}""".trimMargin()

        val input = ApiGatewayInput.read(s)

        System.err.println(input)

    }

}