package codes.rik.window.post.environment

import codes.rik.window.post.process.PostImageProcessor
import codes.rik.window.post.process.ProcessedImagesDao
import codes.rik.window.post.process.S3ProcessedImagesDao
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
    val s3Client: AmazonS3
    val dynamoDbClient: AmazonDynamoDB

    val uploadDao: UploadDao
    val statusDao: StatusDao
    val processedImagesDao: ProcessedImagesDao

    val uploadManager: UploadManager
    val postImageProcessor: PostImageProcessor
}

class DefaultEnvironmentInstance: EnvironmentInstance {
    override val s3Client = AmazonS3ClientBuilder.standard().withRegion(US_WEST_2).build()!!
    override val dynamoDbClient = AmazonDynamoDBClientBuilder.standard().withRegion(US_WEST_2).build()!!

    override val uploadDao by lazy {
        S3UploadDao(s3Client, S3Bucket(Arn(getenv("WINDOW_PostInputBucketArn"))))
    }

    override val statusDao = DynamoStatusDao(dynamoDbClient, DynamoDBTable(Arn(getenv("WINDOW_PostStatusTableArn"))))
    override val processedImagesDao by lazy {
        S3ProcessedImagesDao(this.s3Client, S3Bucket(Arn(getenv("WINDOW_ProcessedImagesBucketArn"))))
    }

    override val uploadManager by lazy {
        UploadManager(uploadDao, statusDao)
    }

    override val postImageProcessor by lazy {
        PostImageProcessor(statusDao, s3Client, processedImagesDao)
    }
}

private val logger = KotlinLogging.logger {}
object Environment {
    val current = DefaultEnvironmentInstance()

    init {
        logger.debug { "Environment: $current" }
        logger.debug { getenv() }
    }
}
