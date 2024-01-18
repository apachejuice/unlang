package dev.apachejuice.unlang.scanner

import dev.apachejuice.unlang.scope.LexicalScope
import dev.apachejuice.unlang.token.Range
import dev.apachejuice.unlang.token.Token

/**
 * A [Scanner] scans lexical elements from source code.
 */
interface Scanner<T: Token<*>> {
    /**
     * The source from which the scanner pulls characters.
     */
    var source: CharacterSource

    /**
     * The scanner's start position.
     */
    val startPos: Int

    /**
     * The range currently being scanned, typically the range between the end of the last token and the current position.
     */
    val range: Range

    /**
     * A set of listeners that are notified when some sort of token is created.
     */
    val tokenListeners: MutableSet<(token: T) -> Unit>

    /**
     * A set of listeners that are notified when a new scope is entered.
     */
    val scopeListeners: MutableSet<(scope: LexicalScope) -> Unit>

    /**
     * Reads the next token from the source and calls the appropiate listeners.
     *
     * Returns true if a token was created.
     */
    fun next(): Boolean
}