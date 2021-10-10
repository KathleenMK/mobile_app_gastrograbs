package org.wit.gastrograbs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.gastrograbs.databinding.CardGrabBinding
import org.wit.gastrograbs.models.GrabModel

interface GrabListener {
    fun onGrabClick(grab: GrabModel)
}
class GrabAdapter constructor(private var grabs: List<GrabModel>,
                                private val listener: GrabListener) :
    RecyclerView.Adapter<GrabAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGrabBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val grab = grabs[holder.adapterPosition]
        holder.bind(grab, listener)
    }

    override fun getItemCount(): Int = grabs.size

    class MainHolder(private val binding: CardGrabBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(grab: GrabModel, listener: GrabListener) {
            binding.grabTitle.text = grab.title
            binding.grabDescription.text = grab.description
            binding.grabCategory.text = grab.category
            binding.root.setOnClickListener{listener.onGrabClick(grab)}        }
    }
}
