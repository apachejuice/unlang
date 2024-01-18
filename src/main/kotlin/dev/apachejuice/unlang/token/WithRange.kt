package dev.apachejuice.unlang.token

/**
 * Any element with an associated [Range].
 */
interface WithRange {
    /**
     * The range
     */
    val range: Range
}