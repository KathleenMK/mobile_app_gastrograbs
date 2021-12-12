package org.wit.gastrograbs.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object GrabManager: GrabStore {

    private val grabs = ArrayList<GrabModel>()

    override fun findAll(): List<GrabModel> {
        return grabs
    }

    override fun findOne(id: Long): GrabModel? {
        return grabs.find { p -> p.id == id }
    }

    override fun create(grab: GrabModel) {
        grab.id = getId()
        grabs.add(grab)
        logAll()
    }

    override fun update(grab: GrabModel) {
        var foundGrab: GrabModel? = grabs.find { p -> p.id == grab.id }
        if (foundGrab != null) {
            foundGrab.title = grab.title
            foundGrab.description = grab.description
            foundGrab.category = grab.category
            foundGrab.image = grab.image
            foundGrab.lat = grab.lat
            foundGrab.lng = grab.lng
            foundGrab.zoom = grab.zoom
            logAll()
        }
    }

    override fun addComment(grab: GrabModel, comment: String) {
        val grabsList = findAll() as java.util.ArrayList<GrabModel>
        var foundGrab: GrabModel? = grabsList.find { p -> p.id == grab.id }
        if (foundGrab != null) {
            foundGrab.comments +=  listOf(comment)
        }
        logAll()
    }

    override fun removeComment(grab: GrabModel, comment: String) {
        val grabsList = findAll() as java.util.ArrayList<GrabModel>
        var foundGrab: GrabModel? = grabsList.find { p -> p.id == grab.id }
        if (foundGrab != null) {
            foundGrab.comments.remove(comment)
        }
    }

    override fun delete(grab: GrabModel) {
        grabs.remove(grab)
    }

    fun logAll(){
        grabs.forEach{i("${it}")}
    }


}