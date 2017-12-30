package codes.rik.window.util

import codes.rik.window.util.UUIDs.randomUUIDString

data class Arn(val arn: String) {
    init {
        require(arn.isNotBlank()) {"ARN must not be blank" }
    }
}

data class PostId(val id: String) {
    companion object {
        fun generate() = PostId(randomUUIDString())
    }
}

data class UserId(val id: String) {
    companion object {
        fun generate() = UserId(randomUUIDString())
    }
}


data class S3Bucket(val arn: Arn, val name: String): AwsResource(arn, name) {
    constructor(arn: Arn) : this(arn, arn.arn.substringAfterLast(":::", ""))
}

data class DynamoDBTable(val arn: Arn, val name: String): AwsResource(arn, name) {
    constructor(arn: Arn) : this(arn, arn.arn.substringAfterLast("/", ""))
}

sealed class AwsResource(arn: Arn, name: String) {
    init {
        require(name.isNotBlank()) { "Bucket name must not be black" }
    }
}