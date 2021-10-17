package org.wit.gastrograbs.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GrabMemStore: GrabStore {

    val grabs = ArrayList<GrabModel>()

    override fun findAll(): List<GrabModel> {
        return grabs
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
            logAll()
        }
    }

    fun logAll(){
        grabs.forEach{i("${it}")}
    }


}