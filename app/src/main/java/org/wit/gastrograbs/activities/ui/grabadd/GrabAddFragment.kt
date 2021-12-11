package org.wit.gastrograbs.activities.ui.grabadd

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.activities.MapsActivity
import org.wit.gastrograbs.databinding.FragmentGrabAddBinding
import org.wit.gastrograbs.helpers.showImagePicker
import org.wit.gastrograbs.main.MainApp
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.Location
import timber.log.Timber

class GrabAddFragment : Fragment() {


    //ADDED IN DEFAULT
//    companion object {
//        fun newInstance() = GrabEditFragment()
//    }

    lateinit var app: MainApp   //added line
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: GrabAddViewModel
    var grab = GrabModel()
    private var _binding: FragmentGrabAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {    //added new onCreate fun
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(GrabAddViewModel::class.java)
        _binding = FragmentGrabAddBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.btnAdd.setOnClickListener() {
            grab.title = binding.grabTitle.text.toString()
            grab.description = binding.grabDescription.text.toString()

            if (binding.categorySpinner.selectedItemPosition != 0) {
                grab.category = binding.categorySpinner.selectedItem.toString()
            } else {
                grab.category = grab.category
            }
            if (grab.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_grab_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                app.grabs.create(grab.copy())
            }

            findNavController().popBackStack()  //https://stackoverflow.com/questions/63760586/kotlin-handling-back-button-click-in-navigation-drawer-android


        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.addLocation.setOnClickListener {
            //var grab = args.grabspecific
            var location = Location(52.15859, -7.14440, 16f)
            if (grab.zoom != 0f) {
                location.lat = grab.lat
                location.lng = grab.lng
                location.zoom = grab.zoom
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


        return root
    }

    //ADDED IN DEFAULT
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(GrabEditViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onResume() {
        super.onResume()
        // something like: detailViewModel.getDonation(args.donationid)
        //render()
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            //var grab = args.grabspecific
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
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
                            //var grab = args.grabspecific
                            Timber.i("Got Result ${result.data!!.data}")
                            binding.grabImage.visibility = View.VISIBLE
                            grab.image = result.data!!.data!!
                            Picasso.get()
                                .load(grab.image)
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
            { //showGrab()
            }
    }

}


