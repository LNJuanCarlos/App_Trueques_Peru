package com.example.truequesperu.Opciones_Login

import android.app.ProgressDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.truequesperu.R
import com.example.truequesperu.databinding.ActivityLoginEmailBinding

class Login_email : AppCompatActivity() {

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets*/

        private lateinit var binding: ActivityLoginEmailBinding
        private lateinit var firebaseAuth : FirebaseAuth
        private lateinit var progressDialog : ProgressDialog
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginEmailBinding.inflate(layoutInflater)
            enableEdgeToEdge()
            setContentView(binding.root)

            firebaseAuth = FirebaseAuth.getInstance()

            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Espere por favor")
            progressDialog.setCanceledOnTouchOutside(false)

            binding.BtnIngresar.setOnClickListener {
                validarInfo()
            }

            binding.TxtRegistrarme.setOnClickListener {
                startActivity(Intent(this@Login_email, Registro_email::class.java))
            }

            binding.TvRecuperar.setOnClickListener {
                startActivity(Intent(this@Login_email, RecuperarPassword::class.java))
            }
        }

        private var email = ""
        private var password = ""
        private fun validarInfo() {

            email = binding.EtEmail.text.toString().trim()
            password = binding.EtPassword.text.toString().trim()

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.EtEmail.error = "Email invalido"
                binding.EtEmail.requestFocus()
            }
            else if (email.isEmpty()){
                binding.EtEmail.error = "Ingrese Email"
                binding.EtEmail.requestFocus()
            }
            else if (password.isEmpty()){
                binding.EtPassword.error = "Ingrese Password"
                binding.EtPassword.requestFocus()
            }
            else{
                loginUsuario()
            }
        }

        private fun loginUsuario() {
            progressDialog.setMessage("Ingresando")
            progressDialog.show()

            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    startActivity(Intent(this,MainActivity::class.java))
                    finishAffinity()
                    Toast.makeText(
                        this,
                        "Bienvenido(a)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener{e->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this,
                        "No se pudo iniciar sesion debido a ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }