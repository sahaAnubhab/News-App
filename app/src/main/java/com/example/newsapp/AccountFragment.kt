package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.FragmentAccountBinding
import com.example.newsapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        setDetails()
        binding.editProfileButton.setOnClickListener{
            val intent = Intent(context, UserDetailsActivity::class.java)
            intent.putExtra("isEdit", "true")
            startActivity(intent)
        }
        binding.signOutButton.setOnClickListener{
            auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
        }
        return binding.root
    }

    private fun setDetails() {
        db.collection("users").document(auth.currentUser!!.uid).get().addOnCompleteListener {
            Log.d("HelloFuckYou", auth.currentUser!!.uid.toString()+it.result.getString("name").toString())
            binding.name.text = it.result.getString("name")
            binding.usertag.text = it.result.getString("userTag")
            binding.phoneNumber.text = it.result.getString("phone")
            Glide.with(this).load(it.result.getString("profilePicLink")).into(binding.profilePicture)
        }.addOnFailureListener {
            Toast.makeText(context, "Could not fetch details", Toast.LENGTH_SHORT).show()
            Log.d("FaliureTORetrieveDEtails", it.message.toString())
        }
    }


}