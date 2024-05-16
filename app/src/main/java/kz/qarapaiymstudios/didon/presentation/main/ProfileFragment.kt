package kz.qarapaiymstudios.didon.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kz.mobydev.draft.presentation.ui.bottomsheet.ExitAccount
import kz.qarapaiymstudios.didon.R
import kz.qarapaiymstudios.didon.databinding.FragmentProfileBinding
import kz.qarapaiymstudios.didon.presentation.adapter.BasketAdapter


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val profileFragmentViewModel:ProfileFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shared = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = shared.getString("email","")
        binding.tvEmailProfile.text = email.toString()
        binding.btnExit.setOnClickListener {
            ExitAccount().show(parentFragmentManager, "")
        }
        observeViewModel()
        binding.button.setOnClickListener {
            val database: DatabaseReference = FirebaseDatabase.getInstance().getReference()
            val ordersRef: DatabaseReference = database.child("orders")

            ordersRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Тапсырыңыз сәтті берілді, тауарлар тез арада сізге жеткізіледі",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { error ->
                    // Обработка ошибки при удалении
                }
        }
    }

    private fun observeViewModel(){
        profileFragmentViewModel.getOrders()
        profileFragmentViewModel.productsLiveData.observe(viewLifecycleOwner, Observer {
            val adapter = BasketAdapter()
            adapter.submitList(it)
            binding.rcViewProfileFragment.adapter = adapter
        })
    }
}