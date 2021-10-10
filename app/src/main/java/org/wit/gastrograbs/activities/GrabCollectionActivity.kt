package org.wit.gastrograbs.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.gastrograbs.R
import org.wit.gastrograbs.databinding.ActivityGrabCollectionBinding
import org.wit.gastrograbs.databinding.CardGrabBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel

class GrabCollectionActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGrabCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrabCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GrabAdapter(app.grabs)
    }
}

    class GrabAdapter constructor(private var grabs: List<GrabModel>) :
        RecyclerView.Adapter<GrabAdapter.MainHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            val binding = CardGrabBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return MainHolder(binding)
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            val grab = grabs[holder.adapterPosition]
            holder.bind(grab)
        }

        override fun getItemCount(): Int = grabs.size

        class MainHolder(private val binding: CardGrabBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(grab: GrabModel) {
                binding.grabTitle.text = grab.title
                binding.grabDescription.text = grab.description
                binding.grabCategory.text = grab.category
            }
        }
    }

