package org.wit.gastrograbs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.databinding.CardGrabImageBinding
import org.wit.gastrograbs.models.GrabModel

interface GrabImageListener {
    fun onGrabClick(grab: GrabModel)
}
class GrabImageAdapter constructor(private var grabs: List<GrabModel>,
                              private val listener: GrabImageListener) :
    RecyclerView.Adapter<GrabImageAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGrabImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val grab = grabs[holder.adapterPosition]
        holder.bind(grab, listener)
    }

    override fun getItemCount(): Int = grabs.size

    class MainHolder(private val binding: CardGrabImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(grab: GrabModel, listener: GrabImageListener) {
            binding.grab = grab
            Picasso.get().load(grab.image.toUri())
                .resize(250, 250)
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.GrabIcon)
            binding.root.setOnClickListener{listener.onGrabClick(grab)}
            binding.executePendingBindings()}   //Include this call to force the bindings to happen immediately
    }
}
