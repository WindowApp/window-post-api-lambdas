package codes.rik.window.api.post.handler

import codes.rik.window.api.post.SubmitPost.SubmitPostRequest
import codes.rik.window.api.post.SubmitPost.SubmitPostResponse
import codes.rik.window.lambda.common.WindowApiGatewayHandler

class CreatePostHandler : WindowApiGatewayHandler<SubmitPostRequest, SubmitPostResponse>(
        SubmitPostRequest.getDefaultInstance()) {

    override fun handle(input: SubmitPostRequest, callback: (SubmitPostResponse) -> Unit) {
        callback(SubmitPostResponse.newBuilder()
                .setBar("hello world!")
                .build())
    }

}