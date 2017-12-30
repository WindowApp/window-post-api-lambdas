package codes.rik.window.post.upload

import codes.rik.window.util.PostId
import codes.rik.window.util.S3Bucket
import codes.rik.window.util.extension.withExpiration
import com.amazonaws.HttpMethod.PUT
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import mu.KotlinLogging
import java.net.URL
import java.time.Instant

private val logger = KotlinLogging.logger {}
class S3UploadDao(
        private val s3: AmazonS3,
        private val inputBucket: S3Bucket): UploadDao {

    override fun createUploadUrl(postId: PostId, expiration: Instant): URL {
        // Create a S3 URL
        logger.debug { "Calling S3 to generate a presigned URL for $postId" }
        return s3.generatePresignedUrl(GeneratePresignedUrlRequest(
                inputBucket.name, // bucket name
                postId.id) // key
                .withMethod(PUT)
                .withExpiration(expiration))
    }
}

interface UploadDao {
    fun createUploadUrl(postId: PostId, expiration: Instant): URL
}