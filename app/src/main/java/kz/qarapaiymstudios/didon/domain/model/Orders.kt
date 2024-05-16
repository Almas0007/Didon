package kz.qarapaiymstudios.didon.domain.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Orders(
    val quantity:Int? = null,
    val total:Int? = null,
    val product:Product? = null
)
