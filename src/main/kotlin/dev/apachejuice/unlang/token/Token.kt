package dev.apachejuice.unlang.token

import dev.apachejuice.unlang.debug.WithDebug

/**
 * Represents a token found in a source.
 */
interface Token<T> : WithRange, WithDebug {
    /**
     * An associated comment.
     */
    val comment: Token<*>?

    /**
     * The content of the token.
     */
    val content: String

    /**
     * The type of the token.
     */
    val type: T
}