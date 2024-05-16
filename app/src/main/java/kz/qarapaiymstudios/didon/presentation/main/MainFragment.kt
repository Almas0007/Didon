package kz.qarapaiymstudios.didon.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kz.qarapaiymstudios.didon.R
import kz.qarapaiymstudios.didon.databinding.ActivityMainBinding
import kz.qarapaiymstudios.didon.databinding.FragmentAddProductBinding
import kz.qarapaiymstudios.didon.databinding.FragmentMainBinding
import kz.qarapaiymstudios.didon.domain.RecyclerItemClickCallback
import kz.qarapaiymstudios.didon.domain.model.Product
import kz.qarapaiymstudios.didon.presentation.adapter.MainListAdapter

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val adapter = MainListAdapter()
    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            navigationAddProductFragment()
        }
        observeViewModel()
    }

    private fun navigationAddProductFragment() {
        val action = MainFragmentDirections.actionMainFragmentToAddProductFragment()
        findNavController().navigate(action)
    }

    private fun observeViewModel() {
        mainFragmentViewModel.getProducts()
        mainFragmentViewModel.productsLiveData.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it)
            binding.tcViewMainFrgment.adapter = adapter
        })
        mainFragmentViewModel.productsIDLiveData.observe(viewLifecycleOwner, Observer {
            adapter.setOnItemClickListener(object: RecyclerItemClickCallback{
                override fun onRecyclerViewItemClick(position: Int) {
                    Log.d("AAA",id.toString())
                    val id:String = it[position]
                    val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(id)
                    findNavController().navigate(action)
                }
            })
        })
    }
}