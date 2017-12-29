package codes.rik.window.util

import com.google.common.base.Preconditions
import java.util.*

data class Arn(val arn: String) {
    init {
        Preconditions.checkArgument(arn.isNotBlank(), "ARN blank")
    }
}

data class PostId(val id: String) {
    companion object {
        fun generate() = PostId(UUID.randomUUID().toString())
    }
}
