package codes.rik.window.util.extension

import java.time.Duration

val Number.seconds
    get() = Duration.ofSeconds(this.toLong())!!

val Number.minutes
    get() = Duration.ofMinutes(this.toLong())!!
