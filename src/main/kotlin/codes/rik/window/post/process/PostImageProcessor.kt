package codes.rik.window.post.process

import codes.rik.window.post.status.PostStatus.DONE
import codes.rik.window.post.status.PostStatus.PROCESSING
import codes.rik.window.post.status.StatusDao
import codes.rik.window.post.upload.S3UploadDao
import codes.rik.window.util.S3Bucket
import com.amazonaws.services.s3.AmazonS3

class PostImageProcessor(
        private val statusDao: StatusDao,
        private val s3: AmazonS3,
        private val processedImagesDao: ProcessedImagesDao) {

    fun processImage(inputBucket: S3Bucket, uploadKey: String) {
        // Retrieve the image bytes
        val uploadedImage = S3UploadDao(s3, inputBucket).getUploadByKey(uploadKey)

        // Flip to processing
        statusDao.updateStatus(uploadedImage.userId, uploadedImage.postId, PROCESSING)

        // For now, let's just write it as-is into the processed dao
        processedImagesDao.saveImage(
                uploadedImage.userId,
                uploadedImage.postId,
                uploadedImage.bytes)

        // TODO: index etc...

        // Flip to done
        statusDao.updateStatus(uploadedImage.userId, uploadedImage.postId, DONE)
    }

}