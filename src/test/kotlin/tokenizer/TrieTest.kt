package tokenizer

import org.junit.Test
import kotlin.test.assertEquals

internal class TrieTest {
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
    private val vocabulary = Vocabulary().apply {
        stubTokenizerVocabulary.forEach {
            add(it)
        }
    }

    @Test
    fun `Trie creates a child for each unique prefix`() {
        val tokenizer = USETokenizer(vocabulary)
        val keys = tokenizer.trie.root.children.keys
        val result = setOf("▁", "a", ".", "I", "l", "i", "k", "e", "t")
        assert(keys.all { result.contains(it) })
        assertEquals(result.size, keys.size)
    }

    @Test
    fun `Trie commonPrefixSearch basic usage`() {
        val tokenizer = USETokenizer(vocabulary)
        val commonPrefixes =
            tokenizer.trie.commonPrefixSearch(arrayOf("l", "i", "k", "e")).map { it.token.joinToString("") }
        val result = arrayOf("l", "like").toSet()
        commonPrefixes.forEach { result.contains(it) }
    }
}