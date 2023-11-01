package com.example.todoii.fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoii.R
import com.example.todoii.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
class SignUpFragment: Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var authentication: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvent()
    }

    private fun registerEvent() {
        binding.apply {
            signUpButton.setOnClickListener{
                val email = binding.editTextEmail.text.toString().trim()
                val password = binding.editTextPassword.text.toString().trim()
                val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

                if (email.isNotEmpty()&&password.isNotEmpty()&&confirmPassword.isNotEmpty()){

                    if (password == confirmPassword){
                        binding.signUpButton.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Snackbar.make(binding.signUpButton,"Registered Successfully", Snackbar.LENGTH_SHORT).show()
                                navController.navigate(R.id.action_signUpFragment_to_homeFragment)

                            }else{
                                Log.v("x",it.exception?.message.toString())
                                val networkConnect = "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
                                if (it.exception?.message.toString() == networkConnect){
                                    Snackbar.make(binding.signUpButton,"Please connect to network ", Snackbar.LENGTH_SHORT).show()
                                }else
                                    Snackbar.make(binding.signUpButton,"${it.exception?.message}", Snackbar.LENGTH_SHORT).show()
                                binding.signUpButton.visibility = View.VISIBLE
                            }

                            binding.progressBar.visibility = View.GONE
                        }
                    }else{
                        Snackbar.make(binding.logoImageView,"Password doesn't match!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            alreadyRegistered.setOnClickListener{
                navController.navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        authentication = FirebaseAuth.getInstance()
    }
}