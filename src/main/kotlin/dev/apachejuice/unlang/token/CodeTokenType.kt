package dev.apachejuice.unlang.token

enum class CodeTokenType(val aggregateOp: CodeTokenType? = null) {
    // Separator characters
    OPEN_BRACE, CLOSE_BRACE, OPEN_BRACKET, CLOSE_BRACKET, OPEN_PAREN, CLOSE_PAREN, COMMA, DOT, COLON, SEMICOLON, AT, QUESTION, HASHTAG,

    // Arithmetic operators
    SLASH, STAR, PLUS, MINUS, PERCENT, STAR_STAR, ASG,

    // Logical operators
    OPEN_ARROW, CLOSE_ARROW, OPEN_ARROW_EQUAL, CLOSE_ARROW_EQUAL, AMP_AMP, WALL_WALL, EQUAL, NOT_EQUAL, NOT,

    // Bit-handling operators
    AMP, WALL, CARET, TILDE, DOUBLE_OPEN_ARROW, DOUBLE_CLOSE_ARROW,

    // Arithmetic aggregate operators
    SLASH_ASG(SLASH), STAR_ASG(STAR), PLUS_ASG(PLUS), MINUS_ASG(MINUS), PERCENT_ASG(PERCENT), STAR_STAR_ASG(STAR_STAR),

    // Logical aggregate operators
    AMP_AMP_ASG(AMP_AMP), WALL_WALL_ASG(WALL_WALL),

    // Bit-handling aggregate operators
    AMP_ASG(AMP), WALL_ASG(WALL), CARET_ASG(CARET), TILDE_ASG(TILDE),
    DOUBLE_OPEN_ARROW_ASG(DOUBLE_OPEN_ARROW), DOUBLE_CLOSE_ARROW_ASG(DOUBLE_CLOSE_ARROW),

    // Values and literals
    STRING_LITERAL, DECIMAL_LITERAL, BINARY_LITERAL, HEXADECIMAL_LITERAL, REAL_LITERAL, IDENTIFIER,

    // Keywords
    KW_FUNC("func"), KW_RETURN("return"), KW_IF("if"), KW_ELSE("else"), KW_USE("use"), KW_MOD("mod"),

    EOF,

    ;

    private var _keyword: String? = null
    val keyword: String?
        get() = _keyword

    constructor(kw: String?) : this() {
        this._keyword = kw
    }
}
