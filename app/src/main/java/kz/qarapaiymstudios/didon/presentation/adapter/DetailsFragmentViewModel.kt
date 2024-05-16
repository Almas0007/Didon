package kz.qarapaiymstudios.didon.presentation.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kz.qarapaiymstudios.didon.domain.model.Product

class DetailsFragmentViewModel : ViewModel() {
    private lateinit var database: DatabaseReference

    private var _productLiveData = MutableLiveData<Product>()
    val productLiveData: LiveData<Product>
        get() = _productLiveData

    init {

    }
    fun getProduct(id: String) {
        database = FirebaseDatabase.getInstance().getReference("products")
        database.child(id).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                val description = dataSnapshot.child("description").getValue(String::class.java) ?: ""
                val location = dataSnapshot.child("location").getValue(String::class.java) ?: ""
                val price = dataSnapshot.child("price").getValue(Int::class.java) ?: 0
                val imgLink = dataSnapshot.child("imgLink").getValue(String::class.java) ?: ""

                val product = Product(name, description, location, price, imgLink)
                _productLiveData.postValue(product)
            }
        }
    }
}