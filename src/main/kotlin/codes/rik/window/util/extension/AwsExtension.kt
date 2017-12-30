package codes.rik.window.util.extension

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import java.time.Instant
import java.util.*

fun GeneratePresignedUrlRequest.withExpiration(instant: Instant) = withExpiration(Date.from(instant))!!

val String.aV get() = AttributeValue().withS(this)!!
val Instant.aV get() = AttributeValue().withN(this.epochSecond.toString())!!

val Map<out DynamoDBField, AttributeValue>.ddb
    get() = this.mapKeys { (k, _) -> k.field }

interface DynamoDBField {
    val field: String
}
