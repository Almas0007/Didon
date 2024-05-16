package kz.qarapaiymstudios.didon.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kz.qarapaiymstudios.didon.domain.model.Product

class MainFragmentViewModel():ViewModel(){
    private val itemIdList = ArrayList<String>()
    private var list = ArrayList<Product>()
    private lateinit var database: DatabaseReference

    private var _productsLiveData = MutableLiveData<List<Product>>()
    val productsLiveData: LiveData<List<Product>>
        get() = _productsLiveData

    private var _productsIDLiveData = MutableLiveData<List<String>>()
    val productsIDLiveData: LiveData<List<String>>
        get() = _productsIDLiveData



    fun getProducts(){
        database = FirebaseDatabase.getInstance().getReference("products")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (item in snapshot.children){
                        item.getValue(Product::class.java)?.let { list.add(it) }
                        itemIdList.add(item.key.toString())
                    }
                    _productsLiveData.postValue(list)
                    _productsIDLiveData.postValue(itemIdList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}