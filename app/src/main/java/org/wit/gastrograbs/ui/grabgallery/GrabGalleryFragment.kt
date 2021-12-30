package org.wit.gastrograbs.ui.grabgallery

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.wit.gastrograbs.R
import org.wit.gastrograbs.adapters.GrabImageAdapter
import org.wit.gastrograbs.adapters.GrabImageListener
import org.wit.gastrograbs.databinding.FragmentGrabGalleryBinding
//import org.wit.gastrograbs.databinding.FragmentHomeBinding
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.ui.auth.LoggedInViewModel

class GrabGalleryFragment : Fragment(), GrabImageListener {

    private lateinit var galleryViewModel: GrabGalleryViewModel //this line vs the below???

    //private val grabCollectionViewModel: GrabCollectionViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private var _binding: FragmentGrabGalleryBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {    //added new onCreate fun
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GrabGalleryViewModel::class.java)

        _binding = FragmentGrabGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //activity?.title = getString(R.string.app_name)  //added this line

        binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        galleryViewModel.observableGrabsList.observe(viewLifecycleOwner, Observer { grabs ->
            grabs?.let { render(grabs) }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGrabClick(grab: GrabModel) {
//        val intent = Intent(activity, GrabViewActivity::class.java)
//        startActivity(intent.putExtra("grab_view",grab))
        // above lines from : https://stackoverflow.com/questions/20835933/intent-from-fragment-to-activity 04Dec21
        // added putExtra in the above to replicate previous activity
        val action = GrabGalleryFragmentDirections.actionNavGalleryToGrabViewFragment(grab)
        findNavController().navigate(action)

    }

    private fun render(grabs: List<GrabModel>) {
        binding.recyclerView.adapter = GrabImageAdapter(grabs, this)
        if (grabs.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.grabsNotFound.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.grabsNotFound.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                galleryViewModel.liveFirebaseUser.value = firebaseUser
                galleryViewModel.loadAll()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gastro_grabs, menu)

        val item = menu.findItem(R.id.toggleGrabs) as MenuItem
        item.setActionView(R.layout.show_toggle_layout)
        val toggleGrabs: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleGrabs.isChecked = true

        toggleGrabs.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                toggleGrabs.setText(R.string.all_grabs)
                galleryViewModel.loadAll()
            } else {
                toggleGrabs.setText(R.string.my_grabs)
                galleryViewModel.load()
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}


