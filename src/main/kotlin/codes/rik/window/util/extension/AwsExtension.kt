package codes.rik.window.util.extension

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import java.time.Instant
import java.util.*

fun GeneratePresignedUrlRequest.withExpiration(instant: Instant) = withExpiration(Date.from(instant))!!