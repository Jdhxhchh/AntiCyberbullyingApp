package com.example.anticyberbullyingapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.anticyberbullyingapp.databinding.ActivityMainBinding
import com.example.anticyberbullyingapp.nlp.TextAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var textAnalyzer: TextAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textAnalyzer = TextAnalyzer(this)

        setupUI()
    }

    private fun setupUI() {
        setupToolbar()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.app_name)
        }

        binding.toolbar.navigationIcon = null
    }

    private fun setupClickListeners() {
        binding.checkButton.setOnClickListener {
            analyzeText()
        }

        binding.literatureBtn.setOnClickListener {
            openLiterature()
        }

        binding.emergencyBtn.setOnClickListener {
            openEmergency()
        }
    }

    private fun analyzeText() {
        val text = binding.inputEditText.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(this, "Будь ласка, введіть текст для перевірки", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.checkButton.isEnabled = false

                val result = withContext(Dispatchers.IO) {
                    textAnalyzer.analyzeText(text)
                }

                displayResults(result)
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Помилка аналізу: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("MainActivity", "Text analysis failed", e)
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.checkButton.isEnabled = true
            }
        }
    }

    private fun displayResults(result: TextAnalyzer.AnalysisResult) {
        binding.apply {
            val editableText = Editable.Factory.getInstance().newEditable(result.highlightedText)
            inputEditText.text = editableText

            speedometerView.updateValue((result.bullyingScore * 100).toInt())

            badWordsCount.text = result.analysisDetails

            resultContainer.visibility = View.VISIBLE
        }
    }

    private fun openLiterature() {
        val intent = Intent(this, LiteratureActivity::class.java)
        startActivity(intent)
    }

    private fun openEmergency() {
        val intent = Intent(this, EmergencyActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_test) {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        textAnalyzer.close()
    }
}