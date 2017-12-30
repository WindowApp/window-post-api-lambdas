package codes.rik.window.util

import kotlin.require as require1

inline fun <T> require(value: T, predicate: (T) -> Boolean?, msg: () -> String): T {
    require1(predicate(value) ?: false, msg)
    return value
}
