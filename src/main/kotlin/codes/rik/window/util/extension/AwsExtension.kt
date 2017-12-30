package codes.rik.window.util.extension

import com.amazonaws.services.dynamodbv2.model.AttributeAction
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import java.time.Instant
import java.util.*

fun GeneratePresignedUrlRequest.withExpiration(instant: Instant) = withExpiration(Date.from(instant))!!

val String.aV get() = AttributeValue().withS(this)!!
val Instant.aV get() = AttributeValue().withN(this.epochSecond.toString())!!

fun AttributeValue.update(action: AttributeAction) = AttributeValueUpdate(this, action)
val AttributeValue.update get() = AttributeValueUpdate().withValue(this)

val <T> Map<out DynamoDBField, T>.ddb
    get() = this.mapKeys { (k, _) -> k.field }

interface DynamoDBField {
    val field: String
}
