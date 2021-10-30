package org.wit.gastrograbs.models

interface GrabberStore {
    fun findAll(): List<GrabberModel>
    fun findOneEmail(email: String): Boolean
    fun authenticate(email: String, password: String): Boolean
    fun signup(grabber: GrabberModel)
    }