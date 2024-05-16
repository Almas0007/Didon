package kz.qarapaiymstudios.didon.presentation.additional

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kz.qarapaiymstudios.didon.R
import kz.qarapaiymstudios.didon.databinding.FragmentDetailsBinding
import kz.qarapaiymstudios.didon.domain.model.Orders
import kz.qarapaiymstudios.didon.domain.model.Product
import kz.qarapaiymstudios.didon.presentation.adapter.DetailsFragmentViewModel

class DetailsFragment : Fragment() {

    lateinit var binding: FragmentDetailsBinding
    private val navArgs: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsFragmentViewModel by viewModels()
    private lateinit var database: DatabaseReference
    private lateinit var databaseOrders: DatabaseReference
    private var quantity: Int = 0
    private var total: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getProduct(navArgs.id)
        viewModel.productLiveData.observe(viewLifecycleOwner, Observer {
            database = Firebase.database.getReference("products")

            binding.apply {
                tvTitle.text = it.name
                tvLocation.text = it.location
                tvDescription.text = it.description
                tvPrice.text = it.price.toString()
                val bytes = Base64.decode(it.imgLink, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgIcon.setImageBitmap(bitmap)


                editTextQuantity.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                        // Выполнить дополнительную валидацию и обработку данных здесь
                        val inputText = editTextQuantity.text.toString()
                        var x = inputText.toIntOrNull() ?: 1 // Преобразование ввода в число, если возможно, иначе 0
                        if (x<1){
                            x = 1
                            total = (x * it.price!!)
                        }else{
                        total = (x * it.price!!)}
                        tvPrice.text = total.toString()

                        // Скрыть клавиатуру
                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editTextQuantity.windowToken, 0)

                        // Вернуть true, чтобы указать, что событие обработано
                        true
                    } else {
                        // Вернуть false, чтобы указать, что событие не обработано и действие по умолчанию должно быть выполнено
                        false
                    }
                }

                btnBuy.setOnClickListener { th ->
                    val quantity = editTextQuantity.text
                    val order = Orders(
                        total = total, quantity = quantity.toString().toIntOrNull()?:1,
                        product = it
                    )
                    databaseOrders = Firebase.database.getReference("orders")
                    val id = databaseOrders.push().key
                    databaseOrders.child(id.toString()).setValue(order).addOnSuccessListener {
                        val action = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                        findNavController().navigate(action)
                    }
                }

            }
        })
    }

}