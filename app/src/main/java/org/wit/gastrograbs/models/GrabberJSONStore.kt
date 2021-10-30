package org.wit.gastrograbs.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.gastrograbs.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE_GRABBER = "gastrograbbers.json"
val gsonBuilderGrabber: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()

val listTypeGrabber: Type = object : TypeToken<ArrayList<GrabberModel>>() {}.type

class GrabberJSONStore(private val context: Context) : GrabberStore {

    var grabbers = mutableListOf<GrabberModel>()

    init {
        if (exists(context, JSON_FILE_GRABBER)) {
            deserialize()
        }
    }
    override fun findAll(): List<GrabberModel> {
        logAll()
        return grabbers
    }

    override fun findOneEmail(email: String): Boolean {
        val grabbersList = findAll() as ArrayList<GrabberModel>
        var foundGrabber: GrabberModel? = grabbersList.find{ g -> g.email == email}
        return foundGrabber != null
    }

    override fun authenticate(email: String, password: String): Boolean {
       val grabbersList = findAll() as ArrayList<GrabberModel>
        var foundGrabber: GrabberModel? = grabbersList.find{ g -> g.email == email}
        if (foundGrabber != null) {
            return foundGrabber.password == password
        }
        else
        {
            return false
        }
    }

    override fun signup(grabber: GrabberModel) {
        grabber.id = generateRandomId()
        grabbers.add(grabber)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilderGrabber.toJson(grabbers, listTypeGrabber)
        write(context, JSON_FILE_GRABBER, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE_GRABBER)
        grabbers = gsonBuilderGrabber.fromJson(jsonString, listTypeGrabber)
    }

    private fun logAll() {
        grabbers.forEach { Timber.i("$it") }
    }
}
