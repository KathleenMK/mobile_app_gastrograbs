package org.wit.gastrograbs.ui.grabadd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.activities.MapsActivity
import org.wit.gastrograbs.databinding.FragmentGrabAddBinding
import org.wit.gastrograbs.helpers.showImagePicker
import org.wit.gastrograbs.models.GrabModel
import org.wit.gastrograbs.models.Location
import org.wit.gastrograbs.ui.auth.LoggedInViewModel
import timber.log.Timber
import org.wit.gastrograbs.firebase.FirebaseDBManager   //Used for programatically adding grabs

class GrabAddFragment : Fragment() {

    //ADDED IN DEFAULT
//    companion object
//        fun newInstance() = GrabEditFragment()
//    }

    //lateinit var app: MainApp   //added line
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: GrabAddViewModel
    //var grab = GrabModel()
    private var _binding: FragmentGrabAddBinding? = null
    private var grabImage: Uri = Uri.EMPTY
    private var grabLat = 0.0 //by Delegates.notNull<Double>()  //tried to :Double, the previous was suggested
    private var grabLng = 0.0  //by Delegates.notNull<Double>()  //'lateinit' modifier is not allowed on properties of primitive types
    private var grabZoom = 0f //by Delegates.notNull<Float>()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {    //added new onCreate fun
        super.onCreate(savedInstanceState)
        //app = activity?.application as MainApp
        setHasOptionsMenu(true)

// FOUR SAMPLE GRABS
//        val firebaseUser = loggedInViewModel.liveFirebaseUser
//        var grab1 = (GrabModel(title="Reeses Overload",description="Chocolatey deliciousness, peanuts and pretzels", category="treat",
//            //arrayListOf("This is the most beautiful Reeses product around!", "In Lidl this week...", "Can someone send me some", "Gorgeous"),
//                                lat=52.16397003708149,lng=-7.162707075476646,zoom=16.0f, email = loggedInViewModel.liveFirebaseUser.value?.email!!))
//        FirebaseDBManager.create(firebaseUser,grab1)
//        var grab2 =(GrabModel(title= "Coffee House Lane Pods", description = "So smooth...", category ="everyday",  email = loggedInViewModel.liveFirebaseUser.value?.email!!))
//        FirebaseDBManager.create(firebaseUser,grab2)
//        var grab3 = (GrabModel(title="Seagull Cruffins",description="If you like croissants and muffins and flavoured custard...",category="weekend",
//            //arrayListOf("Just gorgeous"),
//            lat=52.16130378544212, lng=-7.151395529508591, zoom=16.0f,  email = loggedInViewModel.liveFirebaseUser.value?.email!!))
//        FirebaseDBManager.create(firebaseUser,grab3)
//        var grab4 = (GrabModel(title="Valentia Island Vermouth",description="Really unusual but sweet and great with sparkling wine...",category="treat"))
//        (GrabModel(title="Cinnamon buns",description="Another of Seagull's finest",category="Sweet", lat = 52.16130378544212, lng = -7.151395529508591,
//            zoom = 16.0f,  email = loggedInViewModel.liveFirebaseUser.value?.email!!))
//        FirebaseDBManager.create(firebaseUser,grab4)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(GrabAddViewModel::class.java)
        _binding = FragmentGrabAddBinding.inflate(inflater, container, false)
        val root = binding.root

        viewModel.observableStatus.observe(viewLifecycleOwner, Observer {
            status -> status?.let{render(status)}
        })

        registerImagePickerCallback()
        registerMapCallback()
        registerRefreshCallback()

        binding.btnAdd.setOnClickListener() {
            val title = binding.grabTitle.text.toString()
            val description = binding.grabDescription.text.toString()
            var category = ""
            if (binding.categorySpinner.selectedItemPosition != 0) {
                category = binding.categorySpinner.selectedItem.toString()
            }
//            else {
//                val category = grab.category
//            }
            if (title.isEmpty()) {
                Snackbar.make(it, R.string.enter_grab_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
               viewModel.addGrab(loggedInViewModel.liveFirebaseUser, GrabModel(title = title, description = description,
                                                    category = category, image = grabImage,
                                                    lat = grabLat, lng = grabLng,
                                                    zoom = grabZoom, email = loggedInViewModel.liveFirebaseUser.value?.email!!))
            }

            findNavController().popBackStack()  //https://stackoverflow.com/questions/63760586/kotlin-handling-back-button-click-in-navigation-drawer-android


        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.addLocation.setOnClickListener {
            //var grab = args.grabspecific
            var location = Location(52.15859, -7.14440, 16f)
//            if (grabZoom != 0f) {
//                location.lat = grabLat
//                location.lng = grabLng
//                location.zoom = grabZoom
//            }
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





        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.addGrabError),Toast.LENGTH_LONG).show()
        }
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
                            grabLat = location.lat
                            grabLng = location.lng
                            grabZoom = location.zoom
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
                            grabImage = result.data!!.data!!
                            Picasso.get()
                                .load(grabImage)
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


