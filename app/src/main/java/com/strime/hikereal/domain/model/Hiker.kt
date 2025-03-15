package com.strime.hikereal.domain.model

data class Hiker(
    val id: String,
    val name: String,
    val location: String,
    val distance: Double, // en km
    val activity: String,
    val elevation: Int, // en mètres
    val duration: String,
    val profileImageRes: Int, // Resource ID pour l'image de profil
    val trekCount: Int = 0,
    val hasSentTrek: Boolean = false
)
