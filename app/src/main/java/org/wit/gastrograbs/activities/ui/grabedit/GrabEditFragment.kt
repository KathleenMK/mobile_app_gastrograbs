package org.wit.gastrograbs.activities.ui.grabedit

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.activities.GastroGrabs
import org.wit.gastrograbs.activities.MapsActivity
import org.wit.gastrograbs.adapters.CommentDeleteAdapter
import org.wit.gastrograbs.adapters.CommentListener
import org.wit.gastrograbs.databinding.FragmentGrabEditBinding
import org.wit.gastrograbs.helpers.showImagePicker
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.Location
import timber.log.Timber

class GrabEditFragment : Fragment(), CommentListener {


    //ADDED IN DEFAULT
//    companion object {
//        fun newInstance() = GrabEditFragment()
//    }

    lateinit var app: MainApp   //added line
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editViewModel: GrabEditViewModel
    var grab = GrabModel()
    private val args by navArgs<GrabEditFragmentArgs>()
    private var _binding: FragmentGrabEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var edit = false

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
        editViewModel =
            ViewModelProvider(this).get(GrabEditViewModel::class.java)
        _binding = FragmentGrabEditBinding.inflate(inflater, container, false)
        val root = binding.root

    if (args.grabEdit)
    {edit = args.grabEdit
        grab = args.grabspecific
        render()}

        binding.btnAdd.setOnClickListener() {
            grab.title = binding.grabTitle.text.toString()
            grab.description = binding.grabDescription.text.toString()

            if(binding.categorySpinner.selectedItemPosition != 0){
                grab.category = binding.categorySpinner.selectedItem.toString()
            }
            else{
                grab.category = grab.category
            }
            if (grab.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_grab_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.grabs.update(grab.copy())
                    //i("these are the grab comments in GrabActivity")
                    //i(grab.comments.toString())
                } else {
                    app.grabs.create(grab.copy())
                }

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
            if (grab.zoom != 0f){
                location.lat = grab.lat
                location.lng = grab.lng
                location.zoom = grab.zoom
            }
            val intent = Intent(activity, MapsActivity::class.java)
            startActivity(intent.putExtra("location", location))
            //mapIntentLauncher.launch(launcherIntent)
        }

        val spinner: Spinner = root.findViewById(R.id.category_spinner)  //https://developer.android.com/guide/topics/ui/controls/spinner
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


        return root
    }

    //ADDED IN DEFAULT
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        editViewModel = ViewModelProvider(this).get(GrabEditViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    private fun render() {
//        binding.grabvm = grabViewModel
        var foundGrab = args.grabspecific
        if (foundGrab != null) {
            binding.grabTitle.setText(foundGrab.title)
            binding.grabDescription.setText(foundGrab.description)
            binding.grabCategory.visibility= View.VISIBLE
            binding.grabCategory.setText(foundGrab.category)
            binding.btnAdd.setText(R.string.save_grab)
            //binding.toolbarAdd.setTitle(R.string.title_update)

            Picasso.get()
                .load(foundGrab.image)
                .into(binding.grabImage)
            if (foundGrab.image != Uri.EMPTY) {
                binding.grabImage.visibility=View.VISIBLE
                binding.chooseImage.setText(R.string.change_grab_image)
            }
            if (foundGrab.comments.size > 0) {
                binding.commentHeader.visibility=View.VISIBLE
            }
            if (foundGrab.zoom != 0f){
                binding.addLocation.setText(R.string.change_grab_location)
            }
            //val layoutManager = LinearLayoutManager(this)
            binding.recyclerViewComment.layoutManager = LinearLayoutManager(activity)
            binding.recyclerViewComment.adapter = CommentDeleteAdapter(foundGrab.comments.asReversed(),this)



        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_grab, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.item_delete -> { app.grabs.delete(args.grabspecific)
                //val launcherIntent = Intent(this, GrabCollectionActivity::class.java)
                val intent = Intent(activity, GastroGrabs::class.java)
                startActivity(intent)}

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
                        Timber.i("Got Location ${result.data.toString()}")
                        val location = result.data!!.extras?.getParcelable<Location>("location")!!
                        Timber.i("Location == $location")
                        grab.lat = location.lat
                        grab.lng = location.lng
                        grab.zoom = location.zoom
                        binding.addLocation.setText(R.string.change_grab_location)
                    }
                }
                AppCompatActivity.RESULT_CANCELED -> { } else -> { }
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
                        Timber.i("Got Result ${result.data!!.data}")
                        binding.grabImage.visibility=View.VISIBLE
                        grab.image = result.data!!.data!!
                        Picasso.get()
                            .load(grab.image)
                            .into(binding.grabImage)
                        binding.chooseImage.setText(R.string.change_grab_image)
                    }
                }
                AppCompatActivity.RESULT_CANCELED -> { } else -> { }
            }
        }
}

override fun onCommentClick(comment: String) {
    //var grab = args.grabspecific
    Timber.i("in new listener")
    app.grabs.removeComment(grab,comment)
    showGrab()
}

private fun registerRefreshCallback() {
    refreshIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { //showGrab()
        }
}

private fun showGrab(){ // find updated grab from json after update
    var foundGrab = args.grabspecific
    Timber.i("show grab is +${foundGrab.toString()}")
    if (foundGrab != null) {
        binding.grabTitle.setText(foundGrab.title)
        binding.grabDescription.setText(foundGrab.description)
        binding.grabCategory.visibility= View.VISIBLE
        binding.grabCategory.setText(foundGrab.category)
        binding.btnAdd.setText(R.string.save_grab)
        //binding.toolbarAdd.setTitle(R.string.title_update)

        Picasso.get()
            .load(foundGrab.image)
            .into(binding.grabImage)
        if (foundGrab.image != Uri.EMPTY) {
            binding.grabImage.visibility=View.VISIBLE
            binding.chooseImage.setText(R.string.change_grab_image)
        }
        if (foundGrab.comments.size > 0) {
            binding.commentHeader.visibility=View.VISIBLE
        }
        if (foundGrab.zoom != 0f){
            binding.addLocation.setText(R.string.change_grab_location)
        }
//        val layoutManager = LinearLayoutManager(this)
//        binding.recyclerViewComment.layoutManager = layoutManager
//        binding.recyclerViewComment.adapter = CommentDeleteAdapter(foundGrab.comments.asReversed(),this)
        binding.recyclerViewComment.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewComment.adapter = CommentDeleteAdapter(foundGrab.comments.asReversed(),this)



    }
}


}