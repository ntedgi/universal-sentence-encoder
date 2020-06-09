package tokenizer

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow

internal class USETokenizerTest {
    private val stubTokenizerVocabulary = listOf(
        "�" to 0.0,
        "<s>" to 0.0,
        "</s>" to 0.0,
        "extra_token_id_1" to 0.0,
        "extra_token_id_2" to 0.0,
        "extra_token_id_3" to 0.0,
        "▁" to -2.0,
        "▁a" to -1.0,
        "▁ç" to -2.0,
        "a" to -3.0,
        "." to -1.0,
        "▁I" to -1.0,
        "▁like" to -1.0,
        "▁it" to -1.0,
        "I" to -2.0,
        "like" to -2.0,
        "it" to -2.0,
        "l" to -3.0,
        "i" to -3.0,
        "k" to -3.0,
        "e" to -3.0,
        "i" to -3.0,
        "t" to -3.0
    )
    val vocabulary = Vocabulary().apply {
        stubTokenizerVocabulary.forEach {
            add(it)
        }
    }

    @Test
    fun `basic usage`() {
        val tokenizer = USETokenizer(vocabulary)
        val embed = tokenizer.encode("Ilikeit.")
        val result = arrayOf(11, 15, 16, 10)
        embed.forEachIndexed { i, v ->
            assert(result[i] == v)
        }
    }

    @Test
    fun `handles whitespace`() {
        val tokenizer = USETokenizer(vocabulary)
        val embed = tokenizer.encode("I like it.")
        val result = arrayOf(11, 12, 13, 10)
        embed.forEachIndexed { i, v ->
            assert(result[i] == v)
        }
    }

    @Test
    fun `should normalize inputs`() {
        val tokenizer = USETokenizer(vocabulary)
        val embed1 = tokenizer.encode("ça")
        val embed2 = tokenizer.encode("c\u0327a")
        embed1.forEachIndexed { i, v ->
            assert(embed2[i] == v)
        }
    }

    @Test
    fun `should handle unknown inputs`() {
        val tokenizer = USETokenizer(vocabulary)
        assertDoesNotThrow {
            tokenizer.encode("😹")
        }
    }

    @Test
    fun `should treat consecutive unknown inputs as a single word`() {
        val tokenizer = USETokenizer(vocabulary)
        val embed = tokenizer.encode("a\uD83D\uDE39\uD83D\uDE39")
        val result = arrayOf(7, 0)
        embed.forEachIndexed { i, v ->
            assert(result[i] == v)
        }
    }
}