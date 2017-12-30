package codes.rik.window.util

import codes.rik.window.util.UUIDs.randomUUIDString

data class Arn(val arn: String) {
    init {
        require(arn.isNotBlank()) {"ARN must not be blank" }
    }
}

data class PostId(val id: String): AnyId(id) {
    companion object {
        fun generate() = PostId(randomUUIDString())
    }
}

data class UserId(val id: String): AnyId(id) {
    companion object {
        fun generate() = UserId(randomUUIDString())
    }
}


data class S3Bucket(val arn: Arn, val name: String): AwsResource(name) {
    constructor(arn: Arn) : this(arn, arn.arn.substringAfterLast(":::", ""))
}

data class DynamoDBTable(val arn: Arn, val name: String): AwsResource(name) {
    constructor(arn: Arn) : this(arn, arn.arn.substringAfterLast("/", ""))
}

sealed class AwsResource(name: String) {
    init {
        require(name.isNotBlank()) { "Name for ${javaClass.simpleName} must not be black" }
    }
}

abstract class AnyId(id: String) {
    init {
        require(id.isNotBlank()) { "ID ${javaClass.simpleName} must not be blank" }
    }
}