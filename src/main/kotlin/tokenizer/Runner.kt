// package tokenizer
//
// import org.tensorflow.SavedModelBundle
// import org.tensorflow.Tensor
// import java.io.File
// import java.nio.IntBuffer
// import java.nio.charset.StandardCharsets
//
// object Runner {
//     private val resourceBase = "/home/naort/projects/universal-sentence-encoder"
//     private val path2Vocabulary = "$resourceBase/original-model/vocab.json".toFile()
//     private val modelPath = "$resourceBase/model/large5"
//     private val vocabulary = USETokenizer.loadVocabulary(path2Vocabulary)
//     private val tokenizer = USETokenizer(vocabulary)
//
//     @JvmStatic
//     fun main(args: Array<String>) {
//         val savedModelBundle = SavedModelBundle.load(modelPath, "serve");
//         val posts = arrayOf(
//             "The quick brown fox jumps over the lazy dog.",
//             "I am a sentence for which I would like to get its embedding"
//         )
//         val input = arrayOfNulls<ByteArray>(posts.size)
//         for ((i, post) in posts.withIndex()) {
//             input[i] = post.toByteArray(StandardCharsets.UTF_8)
//         }
//         val t = Tensor.create(input)
//         val embedding = savedModelBundle.session().runner().feed("input", t).fetch("output").run()[0];
//         val output = Array(posts.size) { FloatArray(512) }
//         embedding.copyTo(output)
//         val a1 = output[0]
//         val a2 = output[1]
//         var res = 0f
//         for ((i, v) in a1.withIndex()) {
//             res += v * a2[i]
//         }
//         println(res)
//
//         println(output)
//     }
//
//     private fun String.toFile(): File = File(this)
//     private fun convertInputToTensorMatrix(input: Array<String>): Tensor<*> {
//         val encodings = input.map { tokenizer.encode(it) }
//         val esize = 512
//         val size = encodings.size * esize
//         val buffer = IntBuffer.allocate(size)
//         for (arr in encodings) {
//             for (j in 0 until esize) {
//                 if (j < arr.size) {
//                     buffer.put(arr[j])
//                 } else {
//                     buffer.put(0)
//                 }
//             }
//         }
//         buffer.flip()
//
//         return Tensor.create(longArrayOf(encodings.size.toLong(), esize.toLong()), buffer)
//     }
// }