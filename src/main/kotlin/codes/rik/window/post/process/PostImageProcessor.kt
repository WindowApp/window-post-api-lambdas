package codes.rik.window.post.process

import codes.rik.window.post.status.PostStatus.DONE
import codes.rik.window.post.status.PostStatus.PROCESSING
import codes.rik.window.post.status.StatusDao
import codes.rik.window.post.upload.UploadDao

class PostImageProcessor(
        private val statusDao: StatusDao,
        private val processedImagesDao: ProcessedImagesDao) {

    fun processImage(uploadDao: UploadDao, uploadKey: String) {
        // Retrieve the image bytes
        val uploadedImage = uploadDao.getUploadByKey(uploadKey)

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