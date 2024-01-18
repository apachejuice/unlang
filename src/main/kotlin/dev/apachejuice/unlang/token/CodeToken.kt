package dev.apachejuice.unlang.token

import dev.apachejuice.unlang.debug.CompilationException
import dev.apachejuice.unlang.debug.TagData
import kotlin.math.pow

/**
 * Tokens found in code.
 */
data class CodeToken(
    override val comment: Token<*>?,
    override val content: String,
    override val type: CodeTokenType,
    override val range: Range
) : Token<CodeTokenType> {
    override val tagData: TagData = TagData("CodeToken-$type-${range.tagFormat}").apply {
        put("comment", comment)
        put("content", content)
        put("type", type)
        put("start", range.begin)
        put("end", range.begin)
    }

    /**
     * Converts the contents of the token into a number, if applicable. Returns an instance of the appropiate
     * numerical type.
     *
     * Does not check for syntactical correctness of the number; such should be verified by the scanner.
     */
    fun parseNumber(): NumericValue {
        // Allow '_' to separate digits
        val num = content.replace("_", "")

        when (this.type) {
            CodeTokenType.DECIMAL_LITERAL -> {
                // Scientific notation
                val power = num.indexOfAny(charArrayOf('e', 'E'))
                if (power != -1) {
                    val base = num.substring(0 until power).toInt()
                    val exp = num.substring(power + 1).toInt() // cut off E

                    return NumericValue.Integer(base.toDouble().pow(exp).toInt())
                }

                return NumericValue.Integer(num.toInt())
            }

            CodeTokenType.BINARY_LITERAL -> {
                val sepIdx = num.indexOfAny(charArrayOf('b', 'B'))
                val binary = num.substring(sepIdx + 1)

                return NumericValue.Integer(binary.toInt(2))
            }

            CodeTokenType.HEXADECIMAL_LITERAL -> {
                val sepIdx = num.indexOfAny(charArrayOf('x', 'X'))
                val hexval = num.substring(sepIdx + 1)

                return NumericValue.Integer(hexval.toInt(16))
            }

            CodeTokenType.REAL_LITERAL -> {
                val power = num.indexOfAny(charArrayOf('e', 'E'))
                if (power != -1) {
                    val base = num.substring(0 until power).toDouble()
                    val exp = num.substring(power + 1).toInt() // cut off E

                    return NumericValue.Real(base.pow(exp))
                }

                return NumericValue.Real(num.toDouble())
            }

            else -> throw TokenFormatException("Token is not a number: expected a numerical variety, got $type")
        }
    }
}

/**
 * Represents any numerical value of a token.
 */
sealed class NumericValue {
    class Integer(data: Int) : NumericValue()
    class Real(data: Double) : NumericValue()
}

/**
 * Thrown when a token is used in an incorrect context, for example trying to
 * convert a string literal token into a number.
 */
class TokenFormatException(message: String) : CompilationException(message)