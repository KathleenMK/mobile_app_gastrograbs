package org.wit.gastrograbs.models

interface GrabStore {
    fun findAll(): List<GrabModel>
    fun findOne(id: Long): GrabModel?
    fun create(grab: GrabModel)
    fun update(grab: GrabModel)
    fun addComment(grab: GrabModel, comment: String)
    fun removeComment(grab: GrabModel, comment:String)
    fun delete(grab: GrabModel)
}