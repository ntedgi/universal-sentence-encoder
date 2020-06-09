package tokenizer

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.text.Normalizer

const val RESERVED_SYMBOLS_COUNT = 6;
const val separator = "\u2581"

data class Score(val key: List<String>, val score: Double, val index: Int)

typealias Vocabulary = ArrayList<Pair<String, Double>>

class USETokenizer(val vocabulary: Vocabulary) {
    val trie = Trie()

    init {
        for (i in RESERVED_SYMBOLS_COUNT until vocabulary.size) {
            trie.insert(vocabulary[i].first, vocabulary[i].second, i)
        }
    }

    private fun processInput(str: String): String {
        val nkfcNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFKC)
        return separator + nkfcNormalizedString.replace(" ", separator);
    }

    fun encode(input: String): ArrayList<Int> {
        val nodes = ArrayList<HashMap<Int, ArrayList<Score>>>()
        val words = ArrayList<Int>()
        val best = ArrayList<Double>()
        val input = processInput(input)
        val symbols = Trie.stringToChars(input)
        var i = 0
        while (i <= symbols.size) {
            nodes.add(HashMap())
            words.add(0)
            best.add(0.0)
            i++
        }

        symbols.forEachIndexed { i, s ->
            val prefix = symbols.drop(i)
            val matches = trie.commonPrefixSearch(prefix.toTypedArray())

            matches.forEachIndexed { j, v ->
                val obj = Score(v.token, v.score, v.index)
                val endPos = v.token.size
                if (nodes[i + endPos][i] == null) {
                    nodes[i + endPos][i] = ArrayList()
                }
                nodes[i + endPos][i]!!.add(obj)
            }
        }
        for (endPos in 0 until symbols.size + 1) {
            for (startPos in nodes[endPos]) {
                val arr = startPos.value;
                arr.forEachIndexed { j, word ->
                    val score = word.score + best[endPos - word.key.size]
                    if (best[endPos] == 0.0 || score >= best[endPos]) {
                        best[endPos] = score;
                        words[endPos] = arr[j].index;
                    }
                }
            }
        }
        val results = ArrayList<Int>()
        var iter = words.size - 1
        while (iter > 0) {
            results.add(words[iter])
            iter -= vocabulary[words[iter]].first.length
        }
        val merged = ArrayList<Int>();
        var isPreviousUnk = false;
        for (i in 0 until results.size) {
            val id = results[i];
            if (!(isPreviousUnk && id == 0)) {
                merged.add(id);
            }
            isPreviousUnk = id == 0;
        }
        merged.reverse()
        return merged;
    }

    companion object {
        fun loadVocabulary(path: File): Vocabulary {
            val reuslt = Vocabulary()
            val stream: InputStream = FileInputStream(path)
            JsonReader(InputStreamReader(stream, "UTF-8")).use { reader ->
                val gson = Gson()
                reader.beginArray()
                while (reader.hasNext()) {
                    val arr: JsonArray = gson.fromJson(reader, JsonArray::class.java)

                    try {
                        var num = 0.0
                        val number = arr[1]
                        if (!number.isJsonNull) {
                            num = number.asDouble
                        }
                        reuslt.add(arr[0].asString to num)
                    } catch (e: Exception) {
                        println(arr)
                    }
                }
                reader.endArray()
            }

            return reuslt
        }
    }
}