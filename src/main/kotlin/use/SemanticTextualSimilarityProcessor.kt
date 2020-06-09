package use

import utils.memoizeThreadSafe

class SemanticTextualSimilarityProcessor {
    private fun innerProduct(a: FloatArray, b: FloatArray): Float {
        var res = 0f
        for ((i, v) in a.withIndex()) {
            res += v * b[i]
        }
        return res
    }

    private val fn = { a: FloatArray, b: FloatArray -> innerProduct(a, b) }.memoizeThreadSafe()
    /**
     * Calculate Confusion Matrix of entire array.
     *
     * @param input contains FloatArray of size 512
     * UniversalSentenceEncoder.
     * return matrix input.size X input.size
     */
    fun calc(input: Array<FloatArray>): Array<FloatArray> {
        val result = Array(input.size) { FloatArray(size = input.size) }
        for ((i, a) in result.withIndex())
            for ((j, b) in result.withIndex()) {
                result[i][j] = fn(a, b)
            }
        return result
    }

    fun calc(a: FloatArray, b: FloatArray): Float {
        return fn(a, b)
    }
}
