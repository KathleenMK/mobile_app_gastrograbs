package org.wit.gastrograbs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.gastrograbs.databinding.CardCommentBinding

class CommentAdapter constructor(private var comments: ArrayList<String>) :
    RecyclerView.Adapter<CommentAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCommentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val comment = comments[holder.adapterPosition]
        holder.bind(comment)
    }

    fun removeAt(position: Int) {
        comments.removeAt(position) //only works with ArrayList, so updated comments
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = comments.size

    class MainHolder(private val binding: CardCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: String) {
            binding.comment.text = comment
        }
    }

}