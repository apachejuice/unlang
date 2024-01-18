package dev.apachejuice.unlang.scanner

/**
 * Describes the origin of source code.
 */
class SourceDescriptor private constructor(val type: SourceDescriptorType, val dataName: String) {
    constructor(type: SourceDescriptorType) : this(type, type.defaultName) {
        if (type == SourceDescriptorType.FILE) {
            throw IllegalArgumentException("SourceDescriptor.fromFile() should be called for files")
        }
    }

    override fun toString(): String {
        return "$type:$dataName"
    }

    companion object {
        fun fromFile(fileName: String): SourceDescriptor {
            return SourceDescriptor(SourceDescriptorType.FILE, fileName)
        }
    }
}

enum class SourceDescriptorType {
    FILE,
    INPUT,
    TESTING,
    UNKNOWN;

    internal val defaultName: String
        get() = when (this) {
            FILE -> throw IllegalStateException("No defaultName for SourceDescriptorType.FILE")
            INPUT -> "<input>"
            TESTING -> "<test-input>"
            UNKNOWN -> "<???>"
        }
}

data class SourceReference(val descriptor: SourceDescriptor, var line: Int, var column: Int, var index: Int) {
    fun reachable(other: SourceReference): Boolean {
        return descriptor.dataName == other.descriptor.dataName
    }
}