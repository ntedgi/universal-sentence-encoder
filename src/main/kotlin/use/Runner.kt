package use

fun main(args: Array<String>) {
    val resourceBase = "/home/naort/projects/universal-sentence-encoder"
    val modelPath = "${resourceBase}/model/large5"
    val use = UniversalSentenceEncoder.load(modelPath)
    val stsp = SemanticTextualSimilarityProcessor()
    val posts = arrayOf(
        "The quick brown fox jumps over the lazy dog.", "I am a sentence for which I would like to get its embedding"
    )

    val embedding = use.embed(posts)
    println(stsp.calc(embedding))
    println(stsp.calc(embedding[0],embedding[1]))
}
