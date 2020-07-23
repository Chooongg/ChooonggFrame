package chooongg.frame.http.cache

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cache(
    val time: Long = -1,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
    val forceCacheNoNet: Boolean = true
)