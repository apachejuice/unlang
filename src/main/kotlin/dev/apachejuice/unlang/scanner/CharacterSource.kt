package dev.apachejuice.unlang.scanner

/**
 * Supplies characters, typically to a [Scanner].
 * Handles character classes and matching pairs of characters.
 */
class CharacterSource(val data: String, val descriptor: SourceDescriptor) {
    private val defaultOutOfBounds: (Int) -> Exception = { IndexOutOfBoundsException("Ran out of source at $index") }

    private var index: Int = 0
    private var outOfBounds: (Int) -> Exception = defaultOutOfBounds

    private var column: Int = 1
    private var line: Int = 1

    private val identifierStartCategories: List<CharCategory> = listOf(
        CharCategory.UPPERCASE_LETTER,
        CharCategory.TITLECASE_LETTER,
        CharCategory.MODIFIER_LETTER,
        CharCategory.OTHER_LETTER,
        CharCategory.LOWERCASE_LETTER
    )

    private val identifierPartCategories: List<CharCategory> = identifierStartCategories + listOf(
        CharCategory.DECIMAL_DIGIT_NUMBER,
        CharCategory.OTHER_NUMBER,
        CharCategory.LETTER_NUMBER,
    )

    private val decimalDigit: List<Char> = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    val reference: SourceReference
        get() = SourceReference(descriptor, line, column, index)

    fun fetch(vararg what: Char): Char {
        if (data.length <= index) {
            throw outOfBounds(index)
        }

        val c = data[index]
        if (c !in what && what.isNotEmpty()) {
            return c
        }

        index++
        if (c == '\n') {
            line++
            column = 1
        } else {
            column++
        }

        return c
    }

    private fun fetchExpect(vararg what: Char): Char {
        if (data.length <= index) {
            return data.last()
        }

        return fetch(*what)
    }

    fun expect(vararg what: Char, okFn: ((Char) -> Unit)? = null): ReadResult {
        val c = fetchExpect(*what)
        return ReadResult(c, c in what).let { it.ok { c -> okFn?.invoke(c) } }
    }

    fun identifierStart(): ReadResult {
        val c = fetch()
        return ReadResult(c, c.category in identifierStartCategories)
    }

    fun identifierPart(): ReadResult {
        val c = fetch()
        return ReadResult(c, c.category in identifierPartCategories)
    }

    fun decimalDigit(): ReadResult {
        val c = fetch()
        return ReadResult(c, c in decimalDigit)
    }

    /**
     * [fn] MUST return a valid exception object.
     */
    fun onOutOfBounds(fn: (Int) -> Exception) {
        this.outOfBounds = fn
    }
}

data class ReadResult(val character: Char, val ok: Boolean) {
    fun expect(): Char {
        if (!ok) {
            throw IllegalStateException("Result !ok")
        }

        return this.character
    }

    inline fun ok(block: (Char) -> Unit): ReadResult = ok.let { if (it) block(this.character); return this }
    inline fun not(block: () -> Unit): ReadResult = ok.let { if (!it) block(); return this }
}
