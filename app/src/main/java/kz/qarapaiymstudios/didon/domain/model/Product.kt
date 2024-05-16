package kz.qarapaiymstudios.didon.domain.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Product(
    val name: String? = null,
    val description: String? = null,
    val location: String? = null,
    val price: Int? = null,
    val imgLink: String? = null
)