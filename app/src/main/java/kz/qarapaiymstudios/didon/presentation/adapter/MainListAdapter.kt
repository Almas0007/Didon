package kz.qarapaiymstudios.didon.presentation.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kz.qarapaiymstudios.didon.databinding.AdapterMainListBinding
import kz.qarapaiymstudios.didon.domain.RecyclerItemClickCallback
import kz.qarapaiymstudios.didon.domain.model.Product
import kz.qarapaiymstudios.didon.presentation.main.MainFragmentViewModel

class MainListAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = listOf<Product>()
    private var listenerItem: RecyclerItemClickCallback? = null

    fun setOnItemClickListener(listener: RecyclerItemClickCallback) {
        this.listenerItem = listener
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }

    inner class ViewHolder(private val binding:AdapterMainListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initContent(item: Product) {
            binding.apply {
                tvTitle.text = item.name
                tvLocation.text = item.location
                if (!item.imgLink.isNullOrEmpty()) {
                    val bytes = Base64.decode(item.imgLink, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imgIcon.setImageBitmap(bitmap)
                }
                btnMore.setOnClickListener{
                listenerItem?.onRecyclerViewItemClick(position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            AdapterMainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.initContent(differ.currentList[position])
    }

}