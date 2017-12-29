package codes.rik.window.api.post.environment

import codes.rik.window.util.Arn
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
object Environment {
    val current = getEnvironment()

    init {
        logger.debug { "Environment: $current" }
    }
}

data class EnvironmentInstance(
        val s3Client: AmazonS3,
        val postInputBucketArn: Arn) {

    val postInputBucketName = postInputBucketArn.arn.replace(
            regex = Regex("^.+?:::"),
            replacement = "")
}

private val defaultEnvironment by lazy {
    EnvironmentInstance(
            s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2).build(),
            postInputBucketArn = Arn(System.getenv("WINDOW_PostInputBucketArn")))
}

private val localEnvironment by lazy {
    EnvironmentInstance(
            s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2).build(),
            postInputBucketArn = Arn("arn:aws:s3:::window-post-api-lambdas-postinputbucket-test"))
}

private fun getEnvironment() =
    when(System.getenv("WINDOW_PostInputBucketArn")) {
        "" -> localEnvironment
        else -> defaultEnvironment
    }