package org.wit.gastrograbs.models

interface GrabStore {
    fun findAll(): List<GrabModel>
    fun create(grab: GrabModel)
    fun update(grab: GrabModel)
}