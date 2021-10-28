package org.wit.gastrograbs.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.gastrograbs.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "gastrograbs.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<GrabModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GrabJSONStore(private val context: Context) : GrabStore {

    var grabs = mutableListOf<GrabModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<GrabModel> {
        logAll()
        return grabs
    }

    override fun findOne(id: Long): GrabModel? {
        return grabs.find { p -> p.id == id }
        //serialize()
    }

   override fun create(grab: GrabModel) {
        grab.id = generateRandomId()
        grabs.add(grab)
        serialize()
    }


    override fun update(grab: GrabModel) {
        val grabsList = findAll() as ArrayList<GrabModel>
        var foundGrab: GrabModel? = grabsList.find { p -> p.id == grab.id }
        if (foundGrab != null) {
            foundGrab.title = grab.title
            foundGrab.description = grab.description
            foundGrab.category = grab.category
            foundGrab.image = grab.image
            foundGrab.lat = grab.lat
            foundGrab.lng = grab.lng
            foundGrab.zoom = grab.zoom
            //logAll()
        }
        serialize()
    }

    override fun addComment(grab: GrabModel, comment: String) {
        val grabsList = findAll() as ArrayList<GrabModel>
        var foundGrab: GrabModel? = grabsList.find { p -> p.id == grab.id }
        if (foundGrab != null) {
            foundGrab.comments +=  listOf(comment)
           }
        serialize()
    }

    override fun removeComment(grab: GrabModel, comment: String) {
        val grabsList = findAll() as ArrayList<GrabModel>
        var foundGrab: GrabModel? = grabsList.find { p -> p.id == grab.id }
        if (foundGrab != null) {
            foundGrab.comments.remove(comment)
        }
        serialize()
    }

    override fun delete(grab: GrabModel) {
        grabs.remove(grab)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(grabs, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        grabs = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        grabs.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}