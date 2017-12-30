package codes.rik.window.post.handler.s3

import codes.rik.window.lambda.common.S3Input
import codes.rik.window.lambda.common.WindowLambdaHandler
import mu.KotlinLogging
import java.io.OutputStream

private val logger = KotlinLogging.logger {}
class ProcessUploadHandler: WindowLambdaHandler<S3Input>(S3Input.Companion::read) {
    val uploadDaoRegistry = environment.s3UploadDaoRegistry
    val processor = environment.postImageProcessor

    override fun handle(input: S3Input, output: OutputStream) {
        logger.info { "Processed S3 input: $input" }

        val bucket = input.records[0].s3.bucket.toBucket()
        val uploadKey = input.records[0].s3.`object`.key

        processor.processImage(uploadDaoRegistry.get(bucket), uploadKey)
    }

}

