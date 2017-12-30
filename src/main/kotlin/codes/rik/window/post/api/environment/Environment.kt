package codes.rik.window.post.api.environment

import codes.rik.window.post.status.DynamoStatusDao
import codes.rik.window.post.status.StatusDao
import codes.rik.window.post.upload.S3UploadDao
import codes.rik.window.post.upload.UploadDao
import codes.rik.window.post.upload.UploadManager
import codes.rik.window.util.Arn
import codes.rik.window.util.DynamoDBTable
import codes.rik.window.util.S3Bucket
import com.amazonaws.regions.Regions.US_WEST_2
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import mu.KotlinLogging
import java.lang.System.getenv


interface EnvironmentInstance {
    val postInputBucket: S3Bucket
    val statusTable: DynamoDBTable

    val s3Client: AmazonS3
    val dynamoDbClient: AmazonDynamoDB

    val uploadDao: UploadDao
    val statusDao: StatusDao

    val uploadManager: UploadManager
}

class DefaultEnvironmentInstance: EnvironmentInstance {
    override val postInputBucket = S3Bucket(Arn(getenv("WINDOW_PostInputBucketArn")))
    override val statusTable = DynamoDBTable(Arn(getenv("WINDOW_PostStatusTableArn")))

    override val s3Client = AmazonS3ClientBuilder.standard().withRegion(US_WEST_2).build()!!
    override val dynamoDbClient = AmazonDynamoDBClientBuilder.standard().withRegion(US_WEST_2).build()!!

    override val uploadDao = S3UploadDao(s3Client, postInputBucket)
    override val statusDao = DynamoStatusDao(dynamoDbClient, statusTable)

    override val uploadManager = UploadManager(uploadDao, statusDao)
}

private val logger = KotlinLogging.logger {}
object Environment {
    val current = DefaultEnvironmentInstance()

    init {
        logger.debug { "Environment: $current" }
        logger.debug { getenv() }
    }
}
