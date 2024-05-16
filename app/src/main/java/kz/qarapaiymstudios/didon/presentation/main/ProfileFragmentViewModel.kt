package kz.qarapaiymstudios.didon.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kz.qarapaiymstudios.didon.domain.model.Orders
import kz.qarapaiymstudios.didon.domain.model.Product

class ProfileFragmentViewModel() : ViewModel() {

    private var list = ArrayList<Orders>()
    private lateinit var database: DatabaseReference

    private var _productsLiveData = MutableLiveData<List<Orders>>()
    val productsLiveData: LiveData<List<Orders>>
        get() = _productsLiveData




     fun getOrders() {
        database = FirebaseDatabase.getInstance().getReference("orders")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        item.getValue(Orders::class.java)?.let { list.add(it) }
                    }
                    _productsLiveData.postValue(list)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}