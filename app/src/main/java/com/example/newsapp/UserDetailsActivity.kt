package com.example.newsapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityUserDetailsBinding
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.getStringExtra("isEdit")

        auth = Firebase.auth
        db = Firebase.firestore

        storageReference = Firebase.storage.reference

        if(intent != null){
            db.collection("users").document(auth.currentUser!!.uid).get().addOnCompleteListener {
                binding.name.setText(it.result.get("name").toString())
                binding.phoneNumberEditText.setText(it.result.get("phone").toString())
                binding.userTagEditText.setText(it.result.get("userTag").toString())
                Glide.with(this).load(it.result.getString("profilePicLink")).into(binding.galleryProfilePicView)
            }
        }
        binding.saveButton.setOnClickListener {

            if (binding.name.text.toString().isEmpty() || binding.userTagEditText.text.toString()
                    .isEmpty() || binding.phoneNumberEditText.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "None of the fields can be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveDetails()
        }

        binding.galleryProfilePicView.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            // Start the Intent
            // Start the Intent
            startActivityForResult(galleryIntent, 100)
        }
        binding.selectProfilePicButton.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            // Start the Intent
            // Start the Intent
            startActivityForResult(galleryIntent, 100)
        }

    }

    private fun saveDetails() {
        binding.progressbar.visibility = View.VISIBLE

        val uid = auth.currentUser!!.uid

        val byteArrayOutputStreamProfilePic = ByteArrayOutputStream()
        val drawableProfilePic = binding.galleryProfilePicView.drawable as BitmapDrawable
        val bitmapProgiePic: Bitmap = drawableProfilePic.bitmap
        bitmapProgiePic.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamProfilePic)
        val thumbLogo = byteArrayOutputStreamProfilePic.toByteArray()

        val imagePathProfilePic =
            storageReference.child("company_logo").child(uid + "logo.jpg").putBytes(thumbLogo)

        val task = Tasks.whenAll(imagePathProfilePic)
        task.addOnCompleteListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val url = imagePathProfilePic.result.metadata?.reference?.downloadUrl!!.await()
                val user = User(
                    uid,
                    auth.currentUser!!.email.toString(),
                    binding.name.text.toString(),
                    binding.phoneNumberEditText.text.toString(),
                    binding.userTagEditText.text.toString(),
                    url
                )
                withContext(Dispatchers.Main) {
                    db.collection("users").document(uid)
                        .set(user)
                        .addOnSuccessListener {
                            binding.progressbar.visibility = View.GONE
                            Toast.makeText(
                                this@UserDetailsActivity,
                                "Your details have been saved.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@UserDetailsActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                }.addOnFailureListener {

                    Toast.makeText(
                        this@UserDetailsActivity,
                        "Could not upload details.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Could not upload details", Toast.LENGTH_SHORT).show()
            binding.progressbar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val imageUriLogo = data?.data
            binding.galleryProfilePicView.setImageURI(imageUriLogo)
        }

    }

    override fun onBackPressed() {
        Toast.makeText(this, "You need to fill this details", Toast.LENGTH_SHORT).show()
    }
}

