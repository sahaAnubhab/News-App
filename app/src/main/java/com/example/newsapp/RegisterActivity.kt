package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.newsapp.databinding.ActivityLoginBinding
import com.example.newsapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.alreadyRegisteredClick.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerbutton.setOnClickListener{
            val email = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()
            val repassword = binding.passwordRegister.text.toString()

            if( email.isEmpty() || password.isEmpty() || repassword.isEmpty()){
                Toast.makeText(this, "All fields should be filled.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(password != repassword){
                Toast.makeText(this, "The password does not match with Re- Entered password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createAccount(email, password)

        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        binding.progressbar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    updateUI(null)
                }
            }.addOnFailureListener {
                Toast.makeText(baseContext, " ${it.message.toString()}",
                    Toast.LENGTH_SHORT).show()
            }
        // [END create_user_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
        binding.progressbar.visibility = View.GONE
        if(user!=null){
            val intent = Intent(this, UserDetailsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}