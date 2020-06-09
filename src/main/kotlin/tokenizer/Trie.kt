package tokenizer

class Trie {
    var root: TrieNode = TrieNode(null, false, HashMap(), OutputNode(emptyList(), 0.0, 0))
    fun insert(word: String, score: Double, index: Int) {
        var node: TrieNode = this.root
        val symbols: List<String> = stringToChars(word)
        symbols.forEachIndexed { i, v ->
            if (!node.children.containsKey(symbols[i])) {
                val terms = ArrayList<String>().apply {
                    add(symbols[i])
                    addAll(node.word.token)
                }
                node.children[symbols[i]] = TrieNode(node, false, HashMap(), OutputNode(terms, 0.0, 0))
            }
            node = node.children[symbols[i]]!!
            if (i == symbols.size - 1) {
                node.end = true
                node.word.score = score
                node.word.index = index
            }
        }
    }

    fun commonPrefixSearch(ss: Array<String>): Array<OutputNode> {
        val output = ArrayList<OutputNode>()
        var node = root.children[ss[0]];
        var i = 0

        while (i < ss.size - 1 && node != null) {
            if (node.end) {
                output.add(node.word); }
            node = node.children[ss[i + 1]];
            i++
        }
        if (ss.size == 1 && node != null) {
            output.add(node!!.word)
        }
        if (output.isEmpty()) {
            output.add(OutputNode(listOf(ss[0]), 0.0, 0))
        }

        return output.toTypedArray();
    }

    companion object {
        fun stringToChars(str: String): List<String> = str.split("").filter { it.isNotBlank() }
    }
}

data class OutputNode(val token: List<String>, var score: Double, var index: Int) {
    override fun toString(): String {
        return "( token=${token.joinToString(" ")} | score:${score} | index $index)"
    }
}

data class TrieNode(
    val parent: TrieNode?, var end: Boolean, var children: HashMap<String, TrieNode>, var word: OutputNode
) {
    override fun toString(): String {
        return "( end=$end | children:${children} | word $word)"
    }
}
