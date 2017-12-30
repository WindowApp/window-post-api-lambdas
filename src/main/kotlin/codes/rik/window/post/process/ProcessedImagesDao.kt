package codes.rik.window.post.process

import codes.rik.window.util.PostId
import codes.rik.window.util.S3Bucket
import codes.rik.window.util.UserId
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import java.io.ByteArrayInputStream

class S3ProcessedImagesDao(
        private val s3: AmazonS3,
        private val imagesBucket: S3Bucket): ProcessedImagesDao {

    override fun saveImage(userId: UserId, postId: PostId, imageBytes: ByteArray) {
        s3.putObject(PutObjectRequest(
                imagesBucket.name, // bucketName
                postId.id, // key
                ByteArrayInputStream(imageBytes), // input
                ObjectMetadata().apply { // metadata
                    contentLength = imageBytes.size.toLong()
                    userMetadata = mapOf("userId" to userId.id)
                    // TODO: content type etc...
                }))
    }


}

interface ProcessedImagesDao {
    fun saveImage(userId: UserId, postId: PostId, imageBytes: ByteArray)
}