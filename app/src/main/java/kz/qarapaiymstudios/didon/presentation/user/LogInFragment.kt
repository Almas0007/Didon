package kz.qarapaiymstudios.didon.presentation.user

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kz.qarapaiymstudios.didon.R
import kz.qarapaiymstudios.didon.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogInApp.setOnClickListener {
            val email = binding.editTextEmailLogIn.text.toString().trim()
            val password = binding.editTextPasswordLogIn.text.toString().trim()

            if (isEmailValid(email)) {
                if (isPasswordValid(password)) {
                    loginFirebase(email, password)
                } else {
                    Toast.makeText(requireContext(), "Құпия сөз 6 символдан аз болмау керек", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Email дұрыс форматта жазылмады", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnTextTransitionForRegIn.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToRegInFragment()
            findNavController().navigate(action)
        }
    }
    private fun loginFirebase(email:String, password:String){
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val shared = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
                    shared.edit().putString("email",auth.currentUser?.email).commit()
                   val action = LogInFragmentDirections.actionLogInFragmentToMainFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(),"Жүйеге кіру мүмкін емес, қателік!", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}