package kz.mobydev.draft.presentation.ui.bottomsheet

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.qarapaiymstudios.didon.R
import kz.qarapaiymstudios.didon.databinding.BottomSheetExitBinding

class ExitAccount :BottomSheetDialogFragment() {
    private var binding: BottomSheetExitBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetExitBinding.inflate(layoutInflater)
        return binding?.root
    }
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true
        binding?.run {

            btnLogOut.setOnClickListener {
                val shared = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
                shared.edit().putString("email","no-auth").commit()
                findNavController().navigate(R.id.splashFragment)
                dialog?.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog?.dismiss()

            }
        }

    }

}