package dev.apachejuice.unlang.scanner

import dev.apachejuice.unlang.debug.CompilationException
import dev.apachejuice.unlang.debug.TagData

import dev.apachejuice.unlang.scope.LexicalScope
import dev.apachejuice.unlang.token.CodeToken
import dev.apachejuice.unlang.token.CodeTokenType
import dev.apachejuice.unlang.token.Range
import dev.apachejuice.unlang.token.CodeTokenType.*

/**
 * The actual scanner class for code.
 */
class CodeScanner(override var source: CharacterSource, override val startPos: Int = 0) : Scanner<CodeToken> {
    // Where did the last token start
    private var tokenStartReference: SourceReference = source.reference
    private var eof: Boolean = false

    // Are we scanning whitespace right now, or in a token?
    private var scanningToken: Boolean = false
    private var lastToken: CodeToken? = null

    override val range: Range = tokenStartReference to source.reference
    override val tokenListeners: MutableSet<(CodeToken) -> Unit> = mutableSetOf()
    override val scopeListeners: MutableSet<(scope: LexicalScope) -> Unit> = mutableSetOf()

    val atEof: Boolean
        get() = eof

    init {
        source.onOutOfBounds {
            if (lastToken != null) {
                OutOfBounds(lastToken!!.tagData)
            } else {
                OutOfBounds(TagData("SCANNER_NO_TOKEN").apply { put("index", source.reference.index) })
            }
        }
    }

    /**
     * Thrown on out of bounds, caught internally.
     */
    private class OutOfBounds(tagData: TagData) : CompilationException(tagData)

    private fun notifyToken(type: CodeTokenType, data: Char) = notifyToken(type, data.toString())
    private fun notifyToken(type: CodeTokenType, data: String) {
        val token = CodeToken(null, data, type, tokenStartReference to source.reference)
        tokenListeners.forEach { it.invoke(token) }
        lastToken = token
    }

    override fun next(): Boolean {
        tokenStartReference = source.reference

        try {
            val char = source.fetch()
            if (char in listOf(' ', '\n', '\t', '\r')) {
                scanningToken = false
                return false
            }

            scanningToken = true

            when (char) {
                '{' -> notifyToken(OPEN_BRACE, char)
                '}' -> notifyToken(CLOSE_BRACE, char)
                '[' -> notifyToken(OPEN_BRACKET, char)
                ']' -> notifyToken(CLOSE_BRACKET, char)
                '(' -> notifyToken(OPEN_PAREN, char)
                ')' -> notifyToken(CLOSE_PAREN, char)
                ',' -> notifyToken(COMMA, char)
                '.' -> notifyToken(DOT, char)
                ':' -> notifyToken(COLON, char)
                ';' -> notifyToken(SEMICOLON, char)
                '@' -> notifyToken(AT, char)
                '?' -> notifyToken(QUESTION, char)
                '#' -> notifyToken(HASHTAG, char)
                '/' -> source.expect('=') {
                    notifyToken(SLASH_ASG, "/=")
                }.not {
                    notifyToken(SLASH, "/")
                }

                '*' -> source.expect('*') {
                    source.expect('=') {
                        notifyToken(STAR_STAR_ASG, "**=")
                    }.not {
                        notifyToken(STAR_STAR, "**")
                    }
                }.not {
                    source.expect('=') {
                        notifyToken(STAR_ASG, "*=")
                    }.not {
                        notifyToken(STAR, char)
                    }
                }

                '=' -> source.expect('=') {
                    notifyToken(EQUAL, "==")
                }.not {
                    notifyToken(ASG, char)
                }

                '+' -> source.expect('=') {
                    notifyToken(PLUS_ASG, "+=")
                }.not {
                    notifyToken(PLUS, char)
                }

                '-' -> {
                    source.expect('=') {
                        notifyToken(MINUS_ASG, "-=")
                    }.not {
                        notifyToken(MINUS, char)
                    }
                }

                '%' -> source.expect('=') {
                    notifyToken(PERCENT_ASG, "%=")
                }.not {
                    notifyToken(PERCENT, char)
                }

                '<' -> source.expect('=') {
                    notifyToken(OPEN_ARROW_EQUAL, "<=")
                }.not {
                    source.expect('<') {
                        source.expect('=') {
                            notifyToken(DOUBLE_OPEN_ARROW_ASG, "<<=")
                        }.not {
                            notifyToken(DOUBLE_OPEN_ARROW, "<<")
                        }
                    }.not {
                        notifyToken(OPEN_ARROW, "<")
                    }
                }

                '>' -> source.expect('=') {
                    notifyToken(CLOSE_ARROW_EQUAL, ">=")
                }.not {
                    source.expect('>') {
                        source.expect('=') {
                            notifyToken(DOUBLE_CLOSE_ARROW_ASG, ">>=")
                        }.not {
                            notifyToken(DOUBLE_CLOSE_ARROW, ">>")
                        }
                    }.not {
                        notifyToken(CLOSE_ARROW, char)
                    }
                }

                '!' -> source.expect('=') {
                    notifyToken(NOT_EQUAL, "!=")
                }.not {
                    notifyToken(NOT, char)
                }

                '&' -> source.expect('&') {
                    source.expect('=') {
                        notifyToken(AMP_AMP_ASG, "&&=")
                    }.not {
                        notifyToken(AMP_AMP, "&&")
                    }
                }.not {
                    source.expect('=') {
                        notifyToken(AMP_ASG, "&=")
                    }.not {
                        notifyToken(AMP, char)
                    }
                }

                '|' -> source.expect('|') {
                    source.expect('=') {
                        notifyToken(WALL_WALL_ASG, "||=")
                    }.not {
                        notifyToken(WALL_WALL, "||")
                    }
                }.not {
                    source.expect('=') {
                        notifyToken(WALL_ASG, "|=")
                    }.not {
                        notifyToken(WALL, char)
                    }
                }

                '^' -> source.expect('=') {
                    notifyToken(CARET_ASG, "^=")
                }.not {
                    notifyToken(CARET, char)
                }

                '~' -> source.expect('=') {
                    notifyToken(TILDE_ASG, "~=")
                }.not {
                    notifyToken(TILDE, char)
                }

                else -> {
                    println("no token: $char")
                    scanningToken = false
                }
            }
        } catch (_: OutOfBounds) {
            eof = true
            return false
        }

        return scanningToken
    }
}