package com.app.task1withfirebase

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.task1withfirebase.databinding.ActivityMainBinding
import com.app.task1withfirebase.model.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var arr = arrayOf("C", "Java", "Android", "Kotlin", "Python")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.spCourses.adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, arr)
        var courses = binding.spCourses.selectedItem.toString()
        if (courses.isBlank()) Toast.makeText(this, "Select Course", Toast.LENGTH_SHORT).show()


        binding.button.setOnClickListener {

            var name = binding.etName.text.toString().trim()
            if (name.isBlank()) binding.etName.error = "Enter Name"
            var email = binding.etEmail.text.toString().trim()
            if (email.isBlank()) binding.etEmail.error = "Enter email"
            var pass = binding.etPassword.text.toString().trim()
            if (pass.isBlank()) binding.etPassword.error = "Enter password"
            var courses = binding.spCourses.selectedItem.toString()
            if (courses.isBlank()) Toast.makeText(this, "Select Course", Toast.LENGTH_SHORT).show()

            var gender = ""
            if (binding.rbMale.isChecked) {
                gender = "Male"
            } else if (binding.rbFemale.isChecked) {
                gender = "Female"
            } else Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()

            var usertype = ""
            if (binding.radioButton.isChecked) {
                usertype = "Student"
            } else if (binding.radioButton2.isChecked) {
                usertype = "Teacher"
            } else Toast.makeText(this, "Select User Type", Toast.LENGTH_SHORT).show()

            var mutableList = mutableListOf<String>()
            if (binding.cbEnglish.isChecked) {
                mutableList.add("English")
            } else if (binding.cbHindi.isChecked) {
                mutableList.add("Hindi")
            } else if (binding.cbGujarati.isChecked) {
                mutableList.add("Gujarati")
            } else Toast.makeText(this, "Select Languages", Toast.LENGTH_SHORT).show()

            if(usertype=="Student"){
                database = FirebaseDatabase.getInstance().getReference("Student")
            }else{
                database = FirebaseDatabase.getInstance().getReference("Teacher")
            }

            if (name.isBlank() && email.isBlank() && pass.isBlank() && courses.isBlank() && gender.isBlank() && usertype.isBlank() && mutableList.isEmpty()) {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            } else {
                goforStore(name, email, pass, courses, gender, usertype, mutableList.toString())
                Toast.makeText(this, "data Stored successfully", Toast.LENGTH_SHORT).show()
            }


        }


    }

    private fun goforStore(
        name: String,
        email: String,
        pass: String,
        courses: String,
        gender: String,
        usertype: String,
        mutableList: String
    ) {
        // firebase email and password store.
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "data stored Authentication", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "data not stored", Toast.LENGTH_SHORT).show()
            }
            // firebase realdatabase store data.
            var id = database.push().key!!
            if(usertype=="Student"){
                database = FirebaseDatabase.getInstance().getReference("Student")
            }else{
                database = FirebaseDatabase.getInstance().getReference("Teacher")
            }

            val userref = user(id,name, email, pass, courses, gender, usertype, mutableList)
            database.child(id).setValue(userref).addOnSuccessListener {
                binding.etName.text?.clear()
                binding.etEmail.text?.clear()
                binding.etPassword.text?.clear()

                Toast.makeText(this, "data stored in realtime database", Toast.LENGTH_SHORT).show()
            }


        }

    }
}
