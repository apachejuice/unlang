package dev.apachejuice.unlang.token

import dev.apachejuice.unlang.scanner.SourceReference

/**
 * Any element with a [SourceReference].
 */
interface WithReference {
    /**
     * The source reference.
     */
    val reference: SourceReference
}