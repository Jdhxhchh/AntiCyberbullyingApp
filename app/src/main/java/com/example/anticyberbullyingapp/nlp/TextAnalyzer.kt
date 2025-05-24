package com.example.anticyberbullyingapp.nlp

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.LongBuffer
import kotlin.math.sqrt
import com.example.anticyberbullyingapp.R

class TextAnalyzer(context: Context) {
    private val ortEnvironment: OrtEnvironment = OrtEnvironment.getEnvironment()
    private lateinit var ortSession: OrtSession
    private val badWords: List<String>
    private val appContext = context.applicationContext

    private val toxicReferenceEmbedding: FloatArray = FloatArray(768) { 0.1f }

    init {
        badWords = loadBadWords()
        initializeModel()
    }

    private fun loadBadWords(): List<String> {
        return try {
            appContext.resources.openRawResource(R.raw.bad_words)
                .bufferedReader()
                .useLines { lines ->
                    lines.filter { it.isNotBlank() }
                        .map { it.trim().lowercase() }
                        .toList()
                }
        } catch (e: Exception) {
            Log.e("TextAnalyzer", "Error loading bad words", e)
            listOf("сука", "дурак", "ідіот")
        }
    }

    private fun initializeModel() {
        try {
            val modelPath = "models/multilingual-e5-base-v3-onnx-quantized.onnx"
            val inputStream = appContext.assets.open(modelPath)
            val modelFile = File(appContext.cacheDir, "temp_model.onnx")
            FileOutputStream(modelFile).use { output ->
                inputStream.copyTo(output)
            }

            ortSession = ortEnvironment.createSession(modelFile.absolutePath)
            modelFile.delete()
            Log.d("TextAnalyzer", "ONNX model initialized")
        } catch (e: Exception) {
            Log.e("TextAnalyzer", "Model init failed", e)
            throw RuntimeException("Failed to load ONNX model", e)
        }
    }

    data class AnalysisResult(
        val highlightedText: SpannableString,
        val bullyingScore: Float, // 0-1 (0.8 = високий рівень токсичності)
        val badWordsCount: Int,
        val analysisDetails: String
    )

    suspend fun analyzeText(text: String): AnalysisResult = withContext(Dispatchers.IO) {

        val highlightedText = highlightBadWords(text)
        val badWordsCount = countBadWords(text)


        val embedding = analyzeWithModel(text)
        val toxicityScore = if (embedding != null) {
            calculateToxicityScore(embedding)
        } else {
            calculateFallbackScore(text, badWordsCount)
        }


        AnalysisResult(
            highlightedText = highlightedText,
            bullyingScore = toxicityScore,
            badWordsCount = badWordsCount,
            analysisDetails = buildAnalysisDetails(toxicityScore, badWordsCount)
        )
    }

    private fun highlightBadWords(text: String): SpannableString {
        val spannable = SpannableString(text)
        val textLower = text.lowercase()

        badWords.forEach { word ->
            var startIndex = 0
            while (startIndex < text.length) {
                val foundIndex = textLower.indexOf(word, startIndex)
                if (foundIndex == -1) break

                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    foundIndex,
                    foundIndex + word.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                startIndex = foundIndex + word.length
            }
        }
        return spannable
    }

    private fun countBadWords(text: String): Int {
        val textLower = text.lowercase()
        return badWords.count { word -> textLower.contains(word) }
    }

    private suspend fun analyzeWithModel(text: String): FloatArray? {
        if (text.isBlank()) return null

        return try {

            val tokens = text.split("\\s+".toRegex())
                .map { it.hashCode().toLong() % 10000 }
                .take(512)

            val inputIds = tokens.toLongArray()
            val attentionMask = LongArray(inputIds.size) { 1L }


            val inputTensor = OnnxTensor.createTensor(
                ortEnvironment,
                LongBuffer.wrap(inputIds),
                longArrayOf(1, inputIds.size.toLong())
            )
            val maskTensor = OnnxTensor.createTensor(
                ortEnvironment,
                LongBuffer.wrap(attentionMask),
                longArrayOf(1, attentionMask.size.toLong())
            )


            val output = ortSession.run(
                mapOf(
                    "input_ids" to inputTensor,
                    "attention_mask" to maskTensor
                )
            )


            output?.use { sessionResult ->
                sessionResult.get(0)?.value as? FloatArray
            }
        } catch (e: Exception) {
            Log.e("E5", "Помилка аналізу", e)
            null
        }
    }

    private fun calculateToxicityScore(embedding: FloatArray): Float {
        return cosineSimilarity(embedding, toxicReferenceEmbedding).coerceIn(0f, 1f)
    }

    private fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
        require(a.size == b.size) { "Вектори різної довжини" }
        var dot = 0f
        var normA = 0f
        var normB = 0f
        for (i in a.indices) {
            dot += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        return dot / (sqrt(normA) * sqrt(normB))
    }

    private fun calculateFallbackScore(text: String, badWordsCount: Int): Float {
        return when {
            badWordsCount > 3 -> 0.9f
            badWordsCount > 0 -> 0.6f
            text.length > 200 -> 0.3f
            else -> 0.1f
        }
    }

    private fun buildAnalysisDetails(score: Float, badWordsCount: Int): String {
        return when {
            score > 0.75f -> "🔴 Високий рівень токсичності (${(score * 100).toInt()}%)" +
                    "\n• Знайдено образливих слів: $badWordsCount" +
                    "\n• Модель виявила агресивний контекст"

            score > 0.5f -> "🟠 Середній рівень (${(score * 100).toInt()}%)" +
                    "\n• Образливі слова: $badWordsCount" +
                    "\n• Можливий прихований негатив"

            else -> "🟢 Низький рівень (${(score * 100).toInt()}%)" +
                    "\n• Слів зі списку: $badWordsCount" +
                    "\n• Текст безпечний"
        }
    }

    fun close() {
        try {
            ortSession.close()
            ortEnvironment.close()
        } catch (e: Exception) {
            Log.e("TextAnalyzer", "Помилка закриття", e)
        }
    }
}