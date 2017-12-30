package codes.rik.window.post.status

import codes.rik.window.post.status.Field.*
import codes.rik.window.post.status.PostStatus.CREATED
import codes.rik.window.util.DynamoDBTable
import codes.rik.window.util.PostId
import codes.rik.window.util.UserId
import codes.rik.window.util.extension.DynamoDBField
import codes.rik.window.util.extension.aV
import codes.rik.window.util.extension.ddb
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.PutItemRequest
import java.time.Instant
import java.time.Instant.now

class DynamoStatusDao(
        private val dynamoDb: AmazonDynamoDB,
        private val statusTable: DynamoDBTable): StatusDao {

    override fun createPost(userId: UserId, postId: PostId, expiration: Instant) {
        val now = now()

        val postMap = mapOf(
                USER_ID to userId.id.aV,
                POST_ID to postId.id.aV,
                STATUS to CREATED.aV,
                CREATED_TIME to now.aV,
                UPDATED_TIME to now.aV,
                EXPIRATION_TIME to expiration.aV)

        dynamoDb.putItem(PutItemRequest(statusTable.name, postMap.ddb))
    }

}

interface StatusDao {
    fun createPost(userId: UserId, postId: PostId, expiration: Instant)
}



enum class PostStatus {
    CREATED,
    PROCESSING,
    DONE,
    FAILED,
    DELETED;

    val aV get() = AttributeValue(this.name)
}

private enum class Field(override val field: String): DynamoDBField {
    USER_ID("UserId"),
    POST_ID("PostId"),
    STATUS("Status"),
    CREATED_TIME("CreatedTime"),
    UPDATED_TIME("UpdatedTime"),
    EXPIRATION_TIME("ExpirationTime"),
    FAILURE_REASON("failureReason")
}

