package codes.rik.window.post.upload

import codes.rik.window.util.PostId
import codes.rik.window.util.S3Bucket
import codes.rik.window.util.UserId
import codes.rik.window.util.extension.withExpiration
import com.amazonaws.HttpMethod.PUT
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.google.common.io.ByteStreams
import mu.KotlinLogging
import java.net.URL
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

private val logger = KotlinLogging.logger {}
class S3UploadDao(
        private val s3: AmazonS3,
        private val inputBucket: S3Bucket): UploadDao {

    override fun createUploadUrl(userId: UserId, postId: PostId, expiration: Instant): URL {
        // Create a S3 URL
        logger.debug { "Calling S3 to generate a presigned URL for $postId" }
        return s3.generatePresignedUrl(GeneratePresignedUrlRequest(
                inputBucket.name, // bucket name
                postId.id) // key
                .withMethod(PUT)
                .withExpiration(expiration)
                .apply { putCustomRequestHeader("x-amz-meta-userid", userId.id) })
    }

    override fun getUploadByKey(key: String): UploadedImage {
        val obj = s3.getObject(inputBucket.name, key)
        return UploadedImage(
                userId = UserId(obj.objectMetadata.getUserMetaDataOf("userid")),
                postId = PostId(key),
                bytes = ByteStreams.toByteArray(obj.objectContent))
    }
}

class S3UploadDaoRegistry(private val s3: AmazonS3) {
    private val registry: ConcurrentMap<S3Bucket, S3UploadDao> = ConcurrentHashMap()
    fun get(bucket: S3Bucket) = registry.getOrPut(bucket, { S3UploadDao(s3, bucket) })
}

interface UploadDao {
    fun createUploadUrl(userId: UserId, postId: PostId, expiration: Instant): URL
    fun getUploadByKey(key: String): UploadedImage
}

data class UploadedImage(
        val userId: UserId,
        val postId: PostId,
        val bytes: ByteArray)