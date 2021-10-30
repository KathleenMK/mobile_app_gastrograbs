package org.wit.gastrograbs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.CardCommentBinding

interface CommentListener {
    fun onCommentClick(comment: String)
}

class CommentDeleteAdapter constructor(private var comments: List<String>,
                                        private val listener: CommentListener) :
    RecyclerView.Adapter<CommentDeleteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCommentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val comment = comments[holder.adapterPosition]
        holder.bind(comment, listener)
    }

    override fun getItemCount(): Int = comments.size

    class MainHolder(private val binding: CardCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: String, listener: CommentListener) {
            binding.comment.text = comment
            binding.deleteHint.setText(R.string.hint_delete)
            binding.deleteHint.visibility = View.VISIBLE
            binding.root.setOnClickListener { listener.onCommentClick(comment) }
        }

    }
}