// app/src/main/java/com/example/anticyberbullyingapp/OrganizationAdapter.kt
package com.example.anticyberbullyingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anticyberbullyingapp.databinding.ItemOrganizationBinding

class OrganizationAdapter(
    private val organizations: List<Organization>,
    private val onItemClick: (Organization) -> Unit
) : RecyclerView.Adapter<OrganizationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemOrganizationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrganizationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val org = organizations[position]
        holder.binding.orgName.text = org.name
        holder.binding.orgPhone.text = org.phone
        holder.itemView.setOnClickListener { onItemClick(org) }
    }

    override fun getItemCount() = organizations.size
}