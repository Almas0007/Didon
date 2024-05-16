package kz.qarapaiymstudios.didon.presentation.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.qarapaiymstudios.didon.R
import kz.qarapaiymstudios.didon.databinding.FragmentSearchBinding
import kz.qarapaiymstudios.didon.domain.RecyclerItemClickCallback
import kz.qarapaiymstudios.didon.domain.model.Product
import kz.qarapaiymstudios.didon.presentation.adapter.MainListAdapter

class SearchFragment : Fragment() {
   private lateinit var binding:FragmentSearchBinding
    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()
    private val adapter = MainListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.editTextSearchMovie?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ничего не делать перед изменением текста
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    binding?.btnRefreshEditText?.visibility = View.GONE
                }else{
                    binding?.btnRefreshEditText?.visibility = View.VISIBLE
                    binding?.btnRefreshEditText?.setOnClickListener {
                        binding?.editTextSearchMovie?.text?.clear()
                        binding?.searchResultConstraintLayout?.visibility = View.GONE
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.btnSearch.setOnClickListener {

            var inputText = binding.editTextSearchMovie.text
            searchData(inputText.toString())
        }
        observeViewModel()
    }
    private fun observeViewModel(){
        mainFragmentViewModel.productsIDLiveData.observe(viewLifecycleOwner, Observer {
            adapter.setOnItemClickListener(object: RecyclerItemClickCallback {
                override fun onRecyclerViewItemClick(position: Int) {
                    val id:String = it[position]
                    val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(id)
                    findNavController().navigate(action)
                }
            })
        })
    }
    private fun searchData(inputText:String){
        mainFragmentViewModel.getProducts()
        mainFragmentViewModel.productsLiveData.observe(viewLifecycleOwner, Observer {
            val searchList = arrayListOf<Product>()
            for (item in it){

                if (item.name?.toLowerCase() == inputText.toLowerCase()){
                    searchList.add(item)
                }
            }
            Log.d("AAA",searchList.size.toString())
            adapter.submitList(searchList)
            binding.rcViewSearchFragment.adapter = adapter
        })
    }
}