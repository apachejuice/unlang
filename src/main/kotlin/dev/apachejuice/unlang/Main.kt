package dev.apachejuice.unlang

import dev.apachejuice.unlang.scanner.CharacterSource
import dev.apachejuice.unlang.scanner.CodeScanner
import dev.apachejuice.unlang.scanner.SourceDescriptor
import dev.apachejuice.unlang.scanner.SourceDescriptorType
import dev.apachejuice.unlang.scope.LexicalScope


fun main() {
    val code = ""
    val scanner = CodeScanner(CharacterSource(code, SourceDescriptor(SourceDescriptorType.TESTING)))
    scanner.tokenListeners.add { println("Got token: $it") }

    while (!scanner.atEof) {
        scanner.next()
    }
}