package codes.rik.window.post.api.handler

import codes.rik.window.lambda.common.WindowApiGatewayHandler
import codes.rik.window.post.api.CreatePost.CreatePostRequest
import codes.rik.window.post.api.CreatePost.CreatePostResponse
import codes.rik.window.util.UserId
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
@Suppress("unused")
class CreatePostHandler : WindowApiGatewayHandler<CreatePostRequest, CreatePostResponse>(
        CreatePostRequest.getDefaultInstance()) {
    private val uploadManager = environment.uploadManager

    override fun handle(input: CreatePostRequest, callback: (CreatePostResponse) -> Unit) {
        val userId = UserId(input.userId ?: "")

        with(uploadManager.createUpload(userId)) {
            logger.info("Got pending post upload: $this")

            // Respond
            callback(CreatePostResponse.newBuilder()
                    .setPostId(postId.id)
                    .setUploadUrl(url.toExternalForm())
                    .setUploadExpirationTime(expiration.toEpochMilli())
                    .build())
        }
    }

}