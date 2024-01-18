package dev.apachejuice.unlang.token

import dev.apachejuice.unlang.scanner.SourceDescriptor
import dev.apachejuice.unlang.scanner.SourceReference

typealias Range = Pair<SourceReference, SourceReference>

val Range.size: Int
    get() = second.index - first.index

val Range.begin: SourceReference
    get() = first

val Range.end: SourceReference
    get() = second

val Range.descriptor: SourceDescriptor
    get() = first.descriptor

val Range.tagFormat: String
    get() = "$size:${begin.index}:${end.index}:'${begin.descriptor.dataName}'"

infix fun SourceReference.to(other: SourceReference): Range {
    1 to 1
    if (!reachable(other)) {
        throw IllegalArgumentException("$other not reachable by $this")
    }

    return Pair(this, other)
}

infix fun Range.to(other: Range): Range {
    return first to other.second
}