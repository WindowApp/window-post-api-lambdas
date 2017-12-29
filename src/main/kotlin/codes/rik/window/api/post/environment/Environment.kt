package codes.rik.window.api.post.environment

import codes.rik.window.util.Arn
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import mu.KotlinLogging
import com.amazonaws.regions.Regions.US_WEST_2

private val logger = KotlinLogging.logger {}
object Environment {
    val current = defaultEnvironment

    init {
        logger.debug { "Environment: $current" }
    }
}

data class EnvironmentInstance(
        val s3Client: AmazonS3,
        val dynamoDbClient: AmazonDynamoDB,
        val postInputBucketArn: Arn) {

    val postInputBucketName = postInputBucketArn.arn.replace(
            regex = Regex("^.+?:::"),
            replacement = "")
}

private val defaultEnvironment by lazy {
    EnvironmentInstance(
            s3Client = AmazonS3ClientBuilder.standard().withRegion(US_WEST_2).build(),
            dynamoDbClient = AmazonDynamoDBClientBuilder.standard().withRegion(US_WEST_2).build(),
            postInputBucketArn = Arn(System.getenv("WINDOW_PostInputBucketArn")))
}
