package org.wit.gastrograbs.ui.grabview

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.gastrograbs.R
import org.wit.gastrograbs.activities.MapsActivity
import org.wit.gastrograbs.adapters.CommentAdapter
import org.wit.gastrograbs.databinding.FragmentGrabViewBinding
import org.wit.gastrograbs.models.Location
import org.wit.gastrograbs.ui.auth.LoggedInViewModel
import timber.log.Timber
import kotlin.math.roundToInt

class GrabViewFragment : Fragment() {

    //lateinit var app: MainApp   //added line
    private lateinit var grabViewModel: GrabViewViewModel
    private val args by navArgs<GrabViewFragmentArgs>()
    private var _binding: FragmentGrabViewBinding? = null
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


//    companion object {
//        fun newInstance() = GrabViewFragment()
//    }


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

        _binding = FragmentGrabViewBinding.inflate(inflater, container, false)
        val root = binding.root
        grabViewModel =
            ViewModelProvider(this).get(GrabViewViewModel::class.java)
        //activity?.title = args.grabspecific.title //no difference
        grabViewModel.observableGrab.observe(viewLifecycleOwner, Observer { render() })

        binding.btnAddComment.setOnClickListener {
            var newComment = binding.newComment.text.toString()
            //var grab = args.grabspecific
            if (newComment.isEmpty()) {
                Snackbar.make(it, R.string.empty_comment, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                //grabViewModel.addComment(grab,newComment)
                args.grabspecific.comments += listOf(newComment)
                grabViewModel.updateGrab(
                    args.grabspecific.userid!!,
                    args.grabspecific.uid!!,
                    args.grabspecific
                ) //comments can be added by any user, so update only user grabs for creator email
                Snackbar.make(it, R.string.added_comment, Snackbar.LENGTH_LONG)
                    .show()
                Timber.i(loggedInViewModel.liveFirebaseUser.value?.uid)
                render()
                binding.newComment.setText("")
            }
        }

        binding.btnAddRating.setOnClickListener {
            var newRating = binding.ratingBar.getRating().toDouble()
            //var grab = args.grabspecific
            if (false) {
                Snackbar.make(it, R.string.empty_comment, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                //grabViewModel.addComment(grab,newComment)
                args.grabspecific.ratings += listOf(newRating)
                args.grabspecific.avrating =
                    (args.grabspecific.ratings.sum() / args.grabspecific.ratings.size).roundToInt()
                        .toString()
                grabViewModel.updateGrab(
                    args.grabspecific.userid!!,
                    args.grabspecific.uid!!,
                    args.grabspecific
                ) //comments can be added by any user, so update only user grabs for creator email
                Snackbar.make(it, R.string.added_rating, Snackbar.LENGTH_LONG)
                    .show()
                Timber.i(loggedInViewModel.liveFirebaseUser.value?.uid)
                binding.ratingBar.rating = 0F
                render()
            }
        }

        binding.btnViewMap.setOnClickListener {
            var location = Location(52.15859, -7.14440, 16f)
            var grab = args.grabspecific
            if (grab.zoom != 0f) {
                location.lat = grab.lat
                location.lng = grab.lng
                location.zoom = grab.zoom
            }
            val intent = Intent(activity, MapsActivity::class.java)
            startActivity(intent.putExtra("location", location))
        }

        render()
        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        grabViewModel = ViewModelProvider(this).get(GrabViewViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    private fun render() {
        grabViewModel.getGrab(args.grabspecific.uid!!)  //need to refresh ratings
        binding.grab = grabViewModel

        binding.grabAddedBy.setText("Added By: " + args.grabspecific.email) //can't seem to add string resource

        if (args.grabspecific.ratings.isNotEmpty()) {
            if (args.grabspecific.ratings.size == 1) {
                binding.grabRatingDetail.setText(" from 1 rating...")
            } else {
                binding.grabRatingDetail.setText(" from " + args.grabspecific.ratings.size.toString() + " ratings...")
            }
        } else {
            binding.grabRatingDetail.setText(" no ratings yet ...")

        }

        if (args.grabspecific.description.isNotEmpty()) {
            binding.grabDescription.visibility = View.VISIBLE
        }
        if (args.grabspecific.category.isNotEmpty()) {
            binding.grabCategory.visibility = View.VISIBLE
        }
        if (args.grabspecific.image != "") { //Uri.EMPTY) {
            binding.grabImage.visibility = View.VISIBLE
            Picasso.get()
                .load(args.grabspecific.image.toUri())
                .into(binding.grabImage)
        }
        if (args.grabspecific.zoom != 0f) {
            binding.btnViewMap.visibility = View.VISIBLE
        }
        if (args.grabspecific.comments.isNotEmpty()) {
            binding.recyclerViewComment.visibility = View.VISIBLE
            binding.recyclerViewComment.layoutManager = LinearLayoutManager(activity)
            binding.recyclerViewComment.adapter =
                CommentAdapter(args.grabspecific.comments)  //.asReversed()
        }


        //binding.grabImage.text = args.grabspecific.description


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (loggedInViewModel.liveFirebaseUser.value?.email == args.grabspecific.email) {    //only allowing edits for creator of grab
            inflater.inflate(R.menu.menu_view, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.item_edit -> {
                val grabEdit = true
//                val intent = Intent(activity, GrabActivity::class.java)
//                startActivity(intent.putExtra("grab_edit", args.grabspecific))}
                val action =
                    GrabViewFragmentDirections.actionGrabViewFragmentToGrabFragment(
                        args.grabspecific,
                        grabEdit
                    )
                findNavController().navigate(action)
            }

            // above lines from : https://stackoverflow.com/questions/20835933/intent-from-fragment-to-activity 04Dec21
            // added putExtra in the above to replicate previous activity
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        grabViewModel.getGrab(args.grabspecific.uid!!)  //why !!
        render()
    }

}

