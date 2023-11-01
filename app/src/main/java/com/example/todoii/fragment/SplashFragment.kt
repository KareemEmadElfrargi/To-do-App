package com.example.todoii.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoii.R
import com.example.todoii.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashFragment: Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var authentication: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authentication = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        var delayMillis:Long = 0
        delayMillis = if (authentication.currentUser != null){
             1000
        }else{
             2000
        }
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            checkStatusOfUser()
        },delayMillis)
    }

    private fun checkStatusOfUser() {
        if (authentication.currentUser != null) {
            navController.navigate(R.id.action_splashFragment_to_homeFragment)
        } else {
            navController.navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }


}