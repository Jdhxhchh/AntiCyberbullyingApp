package com.example.anticyberbullyingapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anticyberbullyingapp.databinding.ActivityLiteratureBinding

class LiteratureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiteratureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiteratureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            setupToolbar()
            setupRecyclerView()
        } catch (e: Exception) {
            handleInitializationError(e)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.literature_title)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
    }

    private fun setupRecyclerView() {

        val literatureList = listOf(
            LiteratureAdapter.LiteratureItem(
                title = getString(R.string.literature_title_1),
                description = getString(R.string.literature_description_1),
                source = getString(R.string.literature_source_1),
                url = "https://www.unicef.org/ukraine/cyberbulling",
                iconRes = R.drawable.ic_article
            ),
            LiteratureAdapter.LiteratureItem(
                title = getString(R.string.literature_title_2),
                description = getString(R.string.literature_description_2),
                source = getString(R.string.literature_source_2),
                url = "https://www.youtube.com/watch?v=D9mVnBKh5yA",
                iconRes = R.drawable.ic_video
            ),
            LiteratureAdapter.LiteratureItem(
                title = getString(R.string.literature_title_3),
                description = getString(R.string.literature_description_3),
                source = getString(R.string.literature_source_3),
                url = "https://nus.org.ua/articles/kiberbuling-yak-diyaty-batkam",
                iconRes = R.drawable.ic_article
            )
            ,
            LiteratureAdapter.LiteratureItem(
                title = "Кібербулінг: як протистояти\n",
                description = "Стаття від supportME",
                source = "SupportMe",
                url = "https://supportme.org.ua/needle-and-bullying/cyberbullying",
                iconRes = R.drawable.ic_article
            ),
            LiteratureAdapter.LiteratureItem(
                title = "Кібербулінг",
                description = "Харківський комп'ютерний фаховий коледж",
                source = "YouTube",
                url = "https://www.youtube.com/watch?v=I7vNGfBuGaI",
                iconRes = R.drawable.ic_video
            ),
            LiteratureAdapter.LiteratureItem(
                title = "Кібербулінг – це не дрібниця!",
                description = "Рубіжанська міська військова адміністрація",
                source = "gov.ua",
                url = "https://rmva.gov.ua/news/1702451133/",
                iconRes = R.drawable.ic_article
            ),
            LiteratureAdapter.LiteratureItem(
                title = "Стоп Кібербулінг",
                description = "Повний гайд з протидії кібербулінгу",
                source = "Кампанія проти кібербуліну",
                url = "https://cyber.bullyingstop.org.ua/",
                iconRes = R.drawable.ic_article
            ),
            LiteratureAdapter.LiteratureItem(
                title = "Кібербулінг: як захистити себе та своїх дітей від віртуального насильства?",
                description = "Що варто знати про кібербулінг?",
                source = "Project Kesher",
                url = "https://www.projectkesher.org.ua/news/bezpechnyy-internet-iak-zakhystyty-sebe-ta-svoikh-ditey-vid-kibernasyl-stva/",
                iconRes = R.drawable.ic_article
            ),
            LiteratureAdapter.LiteratureItem(
                title = "Ми проти кібербулінгу в соціальних мережах!",
                description = "Віталій Лобач",
                source = "TikTok",
                url = "https://vm.tiktok.com/ZMBKou4qE/",
                iconRes = R.drawable.ic_video
            ),
            LiteratureAdapter.LiteratureItem(
                title = "Основи життя. Кібербулінг: небезпека в Інтернет-просторі",
                description = "Телеканал D1",
                source = "YouTube",
                url = "https://www.youtube.com/watch?v=2A-xFZ3Dodw",
                iconRes = R.drawable.ic_video
            ),
            LiteratureAdapter.LiteratureItem(
                title = "Кібербулінг. Як себе убезпечити\n",
                description = "Професійні поради психолога",
                source = "YouTube",
                url = "https://www.youtube.com/watch?v=U9l4CFQK9N8",
                iconRes = R.drawable.ic_video
            )
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@LiteratureActivity)
            adapter = LiteratureAdapter(literatureList) { url ->
                openLiteratureItem(url)
            }
            setHasFixedSize(true)
        }
    }

    private fun openLiteratureItem(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("LiteratureActivity", "Failed to open URL", e)
            Toast.makeText(this, getString(R.string.literature_url_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleInitializationError(e: Exception) {
        Log.e("LiteratureActivity", "Initialization error", e)
        Toast.makeText(this, getString(R.string.literature_loading_error), Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}