package org.wit.gastrograbs.models

interface GrabStore {
    fun findAll(): List<GrabModel>
    fun findOne(id: Long): GrabModel?
    fun create(grab: GrabModel)
    fun update(grab: GrabModel)
    fun delete(grab: GrabModel)
}