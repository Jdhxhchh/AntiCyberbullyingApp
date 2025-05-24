package com.example.anticyberbullyingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.example.anticyberbullyingapp.databinding.ItemLiteratureBinding
import com.google.android.material.button.MaterialButton

class LiteratureAdapter(
    private val items: List<LiteratureItem>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<LiteratureAdapter.LiteratureViewHolder>() {

    // Модель даних для елемента літератури
    data class LiteratureItem(
        val title: String,
        val description: String,  // Додано опис
        val source: String,
        val url: String,
        @DrawableRes val iconRes: Int  // ID ресурсу іконки
    )

    inner class LiteratureViewHolder(private val binding: ItemLiteratureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LiteratureItem) {
            with(binding) {
                titleTextView.text = item.title
                descriptionTextView.text = item.description  // Встановлення опису
                sourceTextView.text = item.source

                // Встановлення іконки для кнопки
                (openButton as MaterialButton).setIconResource(item.iconRes)

                // Обробники кліків
                root.setOnClickListener { onClick(item.url) }
                openButton.setOnClickListener { onClick(item.url) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiteratureViewHolder {
        val binding = ItemLiteratureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LiteratureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LiteratureViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}