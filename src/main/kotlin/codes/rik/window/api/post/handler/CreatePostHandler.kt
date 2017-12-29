package codes.rik.window.api.post.handler

import codes.rik.window.util.PostId
import codes.rik.window.api.post.CreatePost.CreatePostRequest
import codes.rik.window.api.post.CreatePost.CreatePostResponse
import codes.rik.window.lambda.common.WindowApiGatewayHandler
import codes.rik.window.util.extension.minutes
import codes.rik.window.util.extension.withExpiration
import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import mu.KotlinLogging
import java.time.Instant.now
import java.util.*

private val logger = KotlinLogging.logger {}
class CreatePostHandler : WindowApiGatewayHandler<CreatePostRequest, CreatePostResponse>(
        CreatePostRequest.getDefaultInstance()) {
    private val s3Client = environment.s3Client
    private val postInputBucketName = environment.postInputBucketName

    override fun handle(input: CreatePostRequest, callback: (CreatePostResponse) -> Unit) {
        // Generate an ID
        val postId = PostId.generate()
        logger.info { "Creating post with ID: $postId" }

        // Generate expiration
        val expiration = now() + 2.minutes

        // Create a S3 URL
        val presignedUrl = s3Client.generatePresignedUrl(GeneratePresignedUrlRequest(
                postInputBucketName, // bucket name
                postId.id) // key
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration))
        logger.info { "Got presigned URL: $presignedUrl" }

        // Respond
        callback(CreatePostResponse.newBuilder()
                .setPostId(postId.id)
                .setUploadUrl(presignedUrl.toExternalForm())
                .setUploadExpirationTime(expiration.toEpochMilli())
                .build())
    }

}