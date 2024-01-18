package dev.apachejuice.unlang.debug

/**
 * Base class for any exceptions thrown by the compiler.
 */
abstract class CompilationException(message: String) : RuntimeException(message) {
    constructor(tagData: TagData) : this(tagData.toString())
    constructor(message: String, tagData: TagData) : this("Message: $message\n$tagData")
    constructor(innerException: Throwable) : this("Caused by: $innerException")
    constructor(innerException: Throwable, tagData: TagData) : this("Caused by: $innerException\n$tagData")
    constructor(
        innerException: Throwable,
        message: String,
        tagData: TagData
    ) : this("Message: $message\nCaused by: $innerException\n$tagData")
}