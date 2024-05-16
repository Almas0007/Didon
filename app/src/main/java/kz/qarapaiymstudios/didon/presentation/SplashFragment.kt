package kz.qarapaiymstudios.didon.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.qarapaiymstudios.didon.R


class SplashFragment : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope.launch {
            delay(2000) // Задержка в 2 секунды

            val shared = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
            val email = shared.getString("email","")
            if ("no-auth" == email) {
                navigateToLoginFragment()
            } else if (email != null && email.isNotEmpty()) {
                navigateToLoginFragment()
            } else {
                navigateToMainFragment()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel() // Отменить все запущенные корутины при уничтожении фрагмента
    }

    private fun navigateToLoginFragment() {
        val action = SplashFragmentDirections.actionSplashFragmentToLogInFragment()
        findNavController().navigate(action)
    }
    private fun navigateToMainFragment() {
        val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
        findNavController().navigate(action)
    }
}