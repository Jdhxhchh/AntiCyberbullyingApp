package com.example.anticyberbullyingapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.anticyberbullyingapp.databinding.ActivityEmergencyBinding

class EmergencyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmergencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            binding = ActivityEmergencyBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Налаштування ActionBar
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = getString(R.string.hotline_title) // Використовуємо ресурс
            }

            // Ініціалізація кнопки "Назад"
            binding.btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            // Ініціалізація інших кнопок
            binding.callEmergencyBtn.setOnClickListener {
                makePhoneCall(getString(R.string.emergency_phone)) // Використовуємо ресурс
            }

            binding.callChildHelplineBtn.setOnClickListener {
                makePhoneCall(getString(R.string.child_helpline_phone_filtered)) // Без пробілів
            }

            binding.callCyberbullyingBtn.setOnClickListener {
                makePhoneCall(getString(R.string.cyberbullying_phone_filtered)) // Без пробілів
            }

            binding.chatBotBtn.setOnClickListener {
                openTelegramBot()
            }

        } catch (e: Exception) {
            Log.e("EmergencyActivity", "Помилка ініціалізації", e)
            Toast.makeText(this, "Помилка при відкритті екрану", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun openTelegramBot() {
        try {
            val botUsername = "kiberpes_bot"
            val telegramUri = Uri.parse("tg://resolve?domain=$botUsername")

            val intent = Intent(Intent.ACTION_VIEW, telegramUri).apply {
                `package` = "org.telegram.messenger"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                val webUri = Uri.parse("https://t.me/$botUsername")
                startActivity(Intent(Intent.ACTION_VIEW, webUri))
            }
        } catch (e: Exception) {
            Log.e("EmergencyActivity", "Помилка відкриття Telegram", e)
            Toast.makeText(this, "Не вдалося відкрити чат-бот", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makePhoneCall(number: String) {
        try {

            val cleanNumber = number.replace("\\s+".toRegex(), "")
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$cleanNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Додаток для дзвінків не знайдено", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("EmergencyActivity", "Помилка дзвінка", e)
            Toast.makeText(this, "Не вдалося здійснити дзвінок", Toast.LENGTH_SHORT).show()
        }
    }
}