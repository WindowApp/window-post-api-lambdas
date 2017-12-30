package codes.rik.window.post.upload

import codes.rik.window.post.status.StatusDao
import codes.rik.window.util.PostId
import codes.rik.window.util.UserId
import codes.rik.window.util.extension.minutes
import mu.KotlinLogging
import java.net.URL
import java.time.Instant
import java.time.Instant.now

private val UPLOAD_EXPIRATION = 10.minutes // TODO
private val logger = KotlinLogging.logger {}

class UploadManager(
        private val uploadDao: UploadDao,
        private val statusDao: StatusDao) {

    fun createUpload(userId: UserId): PendingUpload {
        // Generate an ID
        val postId = PostId.generate()
        logger.debug { "Creating post with ID: $postId" }

        // Generate expiration
        val expiration = now() + UPLOAD_EXPIRATION

        // Create status entry (starts at CREATED)
        statusDao.createPost(userId, postId, expiration)

        // Generate a temporary upload link
        val uploadUrl = uploadDao.createUploadUrl(postId, expiration)
        logger.debug { "Created upload link: $uploadUrl" }

        return PendingUpload(postId, uploadUrl, expiration)
    }

    data class PendingUpload(
            val postId: PostId,
            val url: URL,
            val expiration: Instant)

}