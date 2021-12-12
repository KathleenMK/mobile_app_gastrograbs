package org.wit.gastrograbs.activities.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.gastrograbs.R
import org.wit.gastrograbs.adapters.GrabAdapter
import org.wit.gastrograbs.adapters.GrabListener
import org.wit.gastrograbs.databinding.FragmentGrabCollectionBinding
//import org.wit.gastrograbs.databinding.FragmentHomeBinding
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel

class GrabCollectionFragment : Fragment(), GrabListener {

    //lateinit var app: MainApp   //added line
    private lateinit var grabCollectionViewModel: GrabCollectionViewModel
    private var _binding: FragmentGrabCollectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {    //added new onCreate fun
        super.onCreate(savedInstanceState)
        //app = activity?.application as MainApp
        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        grabCollectionViewModel =
            ViewModelProvider(this).get(GrabCollectionViewModel::class.java)

        _binding = FragmentGrabCollectionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        activity?.title = getString(R.string.app_name)  //added this line

        //val textView: TextView = binding.textHome
        grabCollectionViewModel.observableGrabsList.observe(viewLifecycleOwner, Observer {
            grabs -> grabs?.let {render(grabs)}
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        //binding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        //binding.recyclerView.adapter = GrabAdapter(app.grabs.findAll(),this)

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
        val action = GrabCollectionFragmentDirections.actionNavHomeToGrabViewFragment(grab)
        findNavController().navigate(action)

    }

    private fun render(grabs: List<GrabModel>) {
        binding.recyclerView.adapter = GrabAdapter(grabs,this)
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
        grabCollectionViewModel.load()
    }


}