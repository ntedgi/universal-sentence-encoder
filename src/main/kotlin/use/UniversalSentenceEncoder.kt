package use

import org.tensorflow.SavedModelBundle
import org.tensorflow.Tensor
import utils.toFile
import java.io.FileNotFoundException
import java.lang.Exception
import java.nio.charset.StandardCharsets

private val tags = arrayOf("serve")
private const val inputLayer = "input"
private const val outputLayer = "output"
private const val bufferSize = 512

class UniversalSentenceEncoder private constructor(private val model: SavedModelBundle) {
    companion object {
        fun load(path: String): UniversalSentenceEncoder {
            val file = path.toFile()
            if (!file.exists()) {
                throw FileNotFoundException("file not found in path $path")
            }
            if (!file.isDirectory) {
                throw Exception("model path should be a directory")
            }
            val savedModelBundle = SavedModelBundle.load(path, *tags);
            return UniversalSentenceEncoder(savedModelBundle)
        }
    }

    fun embed(input: Array<String>): Array<FloatArray> {
        val buffer = Array(input.size) {i ->
            input[i].toByteArray(StandardCharsets.UTF_8)
        }
        val tensor = Tensor.create(buffer)
        val embedding = model.session().runner().feed(inputLayer, tensor).fetch(outputLayer).run()[0];
        val output = Array(input.size) { FloatArray(bufferSize) }
        embedding.copyTo(output)
        return output
    }
}



