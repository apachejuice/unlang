package dev.apachejuice.unlang.scope

/**
 * A scope that restricts locally named elements, and inherits parent elements.
 */
class LexicalScope(val slug: String) {
    private var parent: LexicalScope? = null
    private val children: MutableList<LexicalScope> = mutableListOf()

    fun append(scope: LexicalScope) {
        scope.parent = this
        children.add(scope)
    }

    override fun toString(): String {
        val content = children.joinToString(separator = "\n") {
            val indent = " ".repeat(it.depth - 1)
            val nextIndent = " ".repeat(it.depth)
            "${it.slug} {\n$nextIndent$it\n$indent}"
        }

        if (parent == null) {
            return "$slug {\n$content\n}"
        }

        return content
    }

    @Suppress("MemberVisibilityCanBePrivate")
    val depth: Int
        get() = if (parent == null) 0 else parent!!.depth + 1
}