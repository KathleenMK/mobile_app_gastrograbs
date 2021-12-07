package org.wit.gastrograbs.activities.ui.grabview

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.adapters.CommentAdapter
import org.wit.gastrograbs.databinding.FragmentGrabViewBinding
import org.wit.gastrograbs.main.MainApp
import timber.log.Timber

class GrabViewFragment : Fragment() {

    lateinit var app: MainApp   //added line
    private lateinit var grabViewModel: GrabViewViewModel
    private val args by navArgs<GrabViewFragmentArgs>()
    private var _binding: FragmentGrabViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


//    companion object {
//        fun newInstance() = GrabViewFragment()
//    }



    override fun onCreate(savedInstanceState: Bundle?) {    //added new onCreate fun
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        grabViewModel =
            ViewModelProvider(this).get(GrabViewViewModel::class.java)
        _binding = FragmentGrabViewBinding.inflate(inflater, container, false)
        val root= binding.root
        //activity?.title = args.grabspecific.title //no difference
        //grabViewModel.observableGrab.observe(viewLifecycleOwner, Observer { render() })
        render()
        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        grabViewModel = ViewModelProvider(this).get(GrabViewViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    private fun render() {
//        binding.grabvm = grabViewModel
        var foundGrab = args.grabspecific
        binding.grabTitle.text = foundGrab.title
        if(foundGrab.description.isNotEmpty()) {
            binding.grabDescription.visibility=View.VISIBLE
            binding.grabDescription.setText(foundGrab.description)
        }
        if(foundGrab.description.isNotEmpty()) {
            binding.grabCategory.visibility = View.VISIBLE
            binding.grabCategory.setText(foundGrab.category)
        }
        if (foundGrab.image != Uri.EMPTY) {
            binding.grabImage.visibility = View.VISIBLE
            Picasso.get()
                .load(foundGrab.image)
                .into(binding.grabImage)
        }
        if (foundGrab.zoom != 0f){
            binding.btnViewMap.visibility = View.VISIBLE
        }
        if (foundGrab.comments.isNotEmpty()) {
            binding.recyclerViewComment.visibility = View.VISIBLE
            binding.recyclerViewComment.layoutManager = LinearLayoutManager(activity)
            binding.recyclerViewComment.adapter =
                CommentAdapter(foundGrab.comments.asReversed())
        }
        //binding.grabImage.text = args.grabspecific.description


    }


}