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
import kz.qarapaiymstudios.didon.databinding.AdapterBasketBinding

import kz.qarapaiymstudios.didon.databinding.AdapterMainListBinding
import kz.qarapaiymstudios.didon.domain.RecyclerItemClickCallback
import kz.qarapaiymstudios.didon.domain.model.Orders
import kz.qarapaiymstudios.didon.domain.model.Product
import kz.qarapaiymstudios.didon.presentation.main.MainFragmentViewModel

class BasketAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = listOf<Orders>()
    private var listenerItem: RecyclerItemClickCallback? = null

    fun setOnItemClickListener(listener: RecyclerItemClickCallback) {
        this.listenerItem = listener
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Orders>() {
        override fun areItemsTheSame(
            oldItem: Orders,
            newItem: Orders
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Orders,
            newItem: Orders
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Orders>) {
        differ.submitList(list)
    }

    inner class ViewHolder(private val binding:AdapterBasketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initContent(item: Orders) {
            binding.apply {
                tvTitle.text = item.product?.name
                tvLocation.text = item.product?.location
                val bytes = Base64.decode(item.product?.imgLink, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgIcon.setImageBitmap(bitmap)
                tvPriceItem.text =item.product?.price.toString()
                tvQuantity.text = item.quantity.toString()
                tvPriceItem2.text = item.total.toString()

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            AdapterBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.initContent(differ.currentList[position])
    }

}