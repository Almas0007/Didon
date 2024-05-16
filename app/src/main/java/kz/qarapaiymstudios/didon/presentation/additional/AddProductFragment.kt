package kz.qarapaiymstudios.didon.presentation.additional

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.IOException
import kz.qarapaiymstudios.didon.R
import kz.qarapaiymstudios.didon.databinding.FragmentAddProductBinding
import kz.qarapaiymstudios.didon.domain.model.Product
import kz.qarapaiymstudios.didon.domain.utils.Constant

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var database: DatabaseReference
    private val PICK_IMAGE_REQUEST_CODE = 1
    private var imgLink = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.getReference("products")
        binding.btnImgUpdate.setOnClickListener{
          openGallery()
        }
        binding.apply {
            binding.btnSaveProduct.setOnClickListener {
                val name = editTextNameProduct.text.toString()
                val description = editTextDescription.text.toString()
                val location = editTextLocation.text.toString()
                val price = editTextPrice.text.toString()

                val parsedPrice = if (price.isNotEmpty()) {
                    price.toInt()
                } else {
                    0
                }
                if (imgLink==""){
                    imgLink = ""
                }
                addNewProduct(name, description, location, parsedPrice, imgLink)
                val action = AddProductFragmentDirections.actionAddProductFragmentToMainFragment()
                findNavController().navigate(action)
            }
        }

    }
    fun addNewProduct(name:String,description:String,location:String, price:Int,imgLink:String) {
        val product = Product(name,description,location,price,imgLink)
        val id = database.push().key
        database.child(id.toString()).setValue(product)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            val bitmap = getBitmapFromUri(imageUri!!)
            binding.tvImgUpdate.setImageBitmap(bitmap)
            Log.d("AAA",bitmap.toString())
            val encodedImage = encodeImageToBase64(bitmap)
            imgLink = encodedImage
            // Передача строки encodedImage в Firebase Realtime Database
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val exif = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
        val orientation = exif?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

}