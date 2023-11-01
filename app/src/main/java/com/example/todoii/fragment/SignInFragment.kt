package com.example.todoii.fragment

import android.annotation.SuppressLint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoii.R
import com.example.todoii.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignInFragment:Fragment() {
    private lateinit var binding: FragmentSignInBinding

    private lateinit var authentication: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvent()
    }


    @SuppressLint("SuspiciousIndentation")
    private fun registerEvent() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = binding.editTextEmail.text.toString().trim()
                val password = binding.editTextPassword.text.toString().trim()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authentication.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Snackbar.make(
                                    binding.logoImageView,
                                    "Login Successfully",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                navController.navigate(R.id.action_signInFragment_to_homeFragment)
                            } else {
                                Snackbar.make(binding.forgotPasswordTextView,"Email or Password incorrect", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                }

                else {
                    Snackbar.make(
                        binding.forgotPasswordTextView,
                        "Empty Filed Not allowed here!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            forgotPasswordTextView.setOnClickListener {
                navController.navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
    }
    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        authentication = FirebaseAuth.getInstance()
    }
}