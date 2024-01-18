package dev.apachejuice.unlang.test

import dev.apachejuice.unlang.scanner.CharacterSource
import dev.apachejuice.unlang.scanner.CodeScanner
import dev.apachejuice.unlang.scanner.SourceDescriptor
import dev.apachejuice.unlang.scanner.SourceDescriptorType
import dev.apachejuice.unlang.token.CodeToken
import dev.apachejuice.unlang.token.CodeTokenType
import dev.apachejuice.unlang.token.CodeTokenType.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.exp

class CodeScannerTest {
    private fun assertTokenList(got: List<CodeTokenType>, wanted: List<CodeTokenType>) {
        assertArrayEquals(got.toTypedArray(), wanted.toTypedArray())
    }

    private fun makeScanner(data: String): CodeScanner {
        return CodeScanner(CharacterSource(data, SourceDescriptor(SourceDescriptorType.TESTING)))
    }

    private fun makeCollector(list: MutableList<CodeToken>): (CodeToken) -> Unit {
        return {
            list.add(it)
        }
    }

    private fun runScanner(scanner: CodeScanner, tokenCollector: (CodeToken) -> Unit) {
        scanner.tokenListeners.add(tokenCollector)

        while (!scanner.atEof) {
            scanner.next()
        }
    }

    private fun scan(data: String): List<CodeToken> {
        val result = mutableListOf<CodeToken>()
        val scanner = makeScanner(data)
        val tokenCollector = makeCollector(result)
        runScanner(scanner, tokenCollector)

        return result
    }

    @Test
    fun testTokenLines() {
        val data = "-\n%\n&\n\n/"
        val expect = listOf(1, 2, 3, 5)
        val got = scan(data).map { it.range.first.line }

        assertArrayEquals(expect.toTypedArray(), got.toTypedArray())
    }

    @Test
    fun testTokenOrder() {
        val data = "-#%"
        val expect = listOf(MINUS, HASHTAG, PERCENT)
        val got = scan(data).map { it.type }

        assertTokenList(got, expect)
    }

    @Test
    fun testGreedyness() {
        val data = "&&& &== |=== ||==="
        val expect = listOf(AMP_AMP, AMP, AMP_ASG, ASG, WALL_ASG, EQUAL, WALL_WALL_ASG, EQUAL)
        val got = scan(data).map { it.type }

        assertTokenList(got, expect)
    }
}