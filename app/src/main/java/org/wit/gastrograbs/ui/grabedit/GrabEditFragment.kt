package org.wit.gastrograbs.ui.grabedit

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.ui.home.GastroGrabs
import org.wit.gastrograbs.activities.MapsActivity
import org.wit.gastrograbs.adapters.CommentAdapter
import org.wit.gastrograbs.databinding.FragmentGrabEditBinding
import org.wit.gastrograbs.helpers.SwipeToDeleteCallback
import org.wit.gastrograbs.helpers.showImagePicker
import org.wit.gastrograbs.models.Location
import org.wit.gastrograbs.ui.auth.LoggedInViewModel
import timber.log.Timber.i

class GrabEditFragment : Fragment() {


    //ADDED IN DEFAULT
//    companion object {
//        fun newInstance() = GrabEditFragment()
//    }

    //lateinit var app: MainApp   //added line
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var editViewModel: GrabEditViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    //var grab = GrabModel()
    private val args by navArgs<GrabEditFragmentArgs>()
    private var _binding: FragmentGrabEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {    //added new onCreate fun
        super.onCreate(savedInstanceState)
        //app = activity?.application as MainApp
        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGrabEditBinding.inflate(inflater, container, false)
        val root = binding.root
        editViewModel =
            ViewModelProvider(this).get(GrabEditViewModel::class.java)
        editViewModel.observableGrab.observe(viewLifecycleOwner, Observer { render() })

        binding.btnAdd.setOnClickListener() {
            args.grabspecific.title = binding.grabTitle.text.toString()
            args.grabspecific.description = binding.grabDescription.text.toString()

            if (binding.categorySpinner.selectedItemPosition != 0) {
                args.grabspecific.category = binding.categorySpinner.selectedItem.toString()
            } else {
                args.grabspecific.category = args.grabspecific.category
            }
            if (args.grabspecific.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_grab_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                i(loggedInViewModel.liveFirebaseUser.value?.uid!!)
                i(args.grabspecific.uid!!)
                //i(editViewModel.observableGrab!!.value!!.title)
                editViewModel.updateGrab(
                    loggedInViewModel.liveFirebaseUser.value?.uid!!,
                    args.grabspecific.uid!!,
                    args.grabspecific
                )
                //editViewModel.updateImage(loggedInViewModel.liveFirebaseUser.value?.uid!!,args.grabspecific.uid!!,args.grabspecific, args.grabspecific.image)
                //Should I be passing something like the following in the above update method: binding.grabvm2?.observableGrab!!.value!!)
                //i("these are the grab comments in GrabActivity")
                //i(grab.comments.toString())


                findNavController().popBackStack()  //https://stackoverflow.com/questions/63760586/kotlin-handling-back-button-click-in-navigation-drawer-android
//                val action =
//                    GrabFragmentDirections.actionGrab 10Dec21FragmentToGrabViewFragment(args.grabspecific)
//                findNavController().navigate(action)
            }

        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.addLocation.setOnClickListener {
            //var grab = args.grabspecific
            var location = Location(52.15859, -7.14440, 16f)
            if (args.grabspecific.zoom != 0f) {
                location.lat = args.grabspecific.lat
                location.lng = args.grabspecific.lng
                location.zoom = args.grabspecific.zoom
            }
            val intent = Intent(activity, MapsActivity::class.java)
            startActivity(intent.putExtra("location", location))
            //mapIntentLauncher.launch(launcherIntent)
        }

        val spinner: Spinner =
            root.findViewById(R.id.category_spinner)  //https://developer.android.com/guide/topics/ui/controls/spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        this.activity?.let {
            ArrayAdapter.createFromResource(
                it,   //https://stackoverflow.com/questions/48055423/spinner-in-a-fragment 08Dec21
                R.array.category_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }


        registerImagePickerCallback()
        registerMapCallback()
        registerRefreshCallback()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //showLoader(loader,"Deleting...")
                val adapter = binding.recyclerViewComment.adapter as CommentAdapter
                //hideLoader(loader)
                adapter.removeAt(viewHolder.adapterPosition)
                editViewModel.updateGrab(
                    loggedInViewModel.liveFirebaseUser.value?.uid!!,
                    args.grabspecific.uid!!,
                    args.grabspecific
                )
                render()
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(binding.recyclerViewComment)


        return root
    }

    //ADDED IN DEFAULT
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        editViewModel = ViewModelProvider(this).get(GrabEditViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    private fun render() {
//        binding.grab = grabViewModel
        var foundGrab = args.grabspecific
        if (foundGrab != null) {
            binding.grabTitle.setText(foundGrab.title)
            binding.grabDescription.setText(foundGrab.description)
            binding.grabCategory.visibility = View.VISIBLE
            binding.grabCategory.setText(foundGrab.category)
            binding.btnAdd.setText(R.string.save_grab)
            //binding.toolbarAdd.setTitle(R.string.title_update)
            if (foundGrab.image != "") {
                Picasso.get()
                    .load(foundGrab.image)
                    .into(binding.grabImage)
            }
            if (foundGrab.image != "") {    //Uri.EMPTY) {
                binding.grabImage.visibility = View.VISIBLE
                binding.chooseImage.setText(R.string.change_grab_image)
            }
            if (foundGrab.comments.size > 0) {
                binding.commentHeader.visibility = View.VISIBLE
            }
            if (foundGrab.zoom != 0f) {
                binding.addLocation.setText(R.string.change_grab_location)
            }
            //val layoutManager = LinearLayoutManager(this)
            binding.recyclerViewComment.layoutManager = LinearLayoutManager(activity)
            binding.recyclerViewComment.adapter = CommentAdapter(foundGrab.comments)


        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_grab, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.item_delete -> {
                editViewModel.deleteGrab(
                    loggedInViewModel.liveFirebaseUser.value?.uid!!,
                    args.grabspecific.uid!!
                )
                //Should I be passing something like the following in the above update method: binding.grabvm2?.observableGrab!!.value!!)
                //val launcherIntent = Intent(this, GrabCollectionActivity::class.java)
                val intent = Intent(activity, GastroGrabs::class.java)
                startActivity(intent)
            }

//            R.id.item_edit -> {
//                val intent = Intent(activity, GrabActivity::class.java)
//                startActivity(intent.putExtra("grab_edit", args.grabspecific))}
            // above lines from : https://stackoverflow.com/questions/20835933/intent-from-fragment-to-activity 04Dec21
            // added putExtra in the above to replicate previous activity
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        // something like: detailViewModel.getDonation(args.donationid)
        render()
    }


    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            var grab = args.grabspecific
                            i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            grab.lat = location.lat
                            grab.lng = location.lng
                            grab.zoom = location.zoom
                            binding.addLocation.setText(R.string.change_grab_location)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            var grab = args.grabspecific
                            i("Got Result ${result.data!!.data}")
                            binding.grabImage.visibility = View.VISIBLE
                            grab.image = result.data!!.data!!.toString()
                            Picasso.get()
                                .load(grab.image.toUri())
                                .into(binding.grabImage)
                            binding.chooseImage.setText(R.string.change_grab_image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
            }
    }

}