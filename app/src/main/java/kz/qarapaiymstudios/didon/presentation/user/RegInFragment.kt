package kz.qarapaiymstudios.didon.presentation.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kz.qarapaiymstudios.didon.databinding.FragmentRegInBinding

class RegInFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var binding: FragmentRegInBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegInBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding?.btnRegInApp?.setOnClickListener {
            val email = binding?.editTextEmailRegIn?.text.toString().trim()
            val password = binding?.editTextPasswordRegIn?.text.toString().trim()

            if (isEmailValid(email)) {
                if (isPasswordValid(password)) {
                    registerUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Құпия сөз 6 символдан аз болмау керек", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Email дұрыс форматта жазылмады", Toast.LENGTH_SHORT).show()
            }
        }



        binding?.btnBackWelcomeFragment?.setOnClickListener {
            val action = RegInFragmentDirections.actionRegInFragmentToLogInFragment()
            findNavController().navigate(action)
        }
    }

    private fun registerUser(email: String, password: String) {
        Log.d("AAA", "$email $password")
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val action = RegInFragmentDirections.actionRegInFragmentToLogInFragment()
                        findNavController().navigate(action)
                        val user = auth.currentUser
                        Log.d("AAA", "createUserWithEmail:success $user")

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("AAA", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(),"Жүйеге тіркелу мүмкін емес, қателік!", Toast.LENGTH_LONG).show()
                    }
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