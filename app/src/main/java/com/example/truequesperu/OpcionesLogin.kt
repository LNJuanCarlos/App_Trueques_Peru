package com.example.truequesperu

import android.app.ProgressDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.truequesperu.databinding.ActivityOpcionesLoginBinding
import com.google.firebase.auth.FirebaseAuth

class OpcionesLogin : AppCompatActivity() {

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets*/
        private lateinit var binding: ActivityOpcionesLoginBinding
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var mGoogleSignInClient : GoogleSignInClient
        private lateinit var progressDialog : ProgressDialog
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityOpcionesLoginBinding.inflate(layoutInflater)
            enableEdgeToEdge()
            setContentView(binding.root)

            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Espere por favor")
            progressDialog.setCanceledOnTouchOutside(false)

            firebaseAuth = FirebaseAuth.getInstance()
            comprobarSesion()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this,gso)



            binding.IngresarEmail.setOnClickListener{
                startActivity(Intent(this@OpcionesLogin, Login_email::class.java))
            }

            binding.IngresarGoogle.setOnClickListener {
                googleLogin()
            }
        }

        private fun googleLogin() {
            val googleSignInIntent = mGoogleSignInClient.signInIntent
            googleSignInARL.launch(googleSignInIntent)
        }

        private val googleSignInARL = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){resultado->
            if(resultado.resultCode == RESULT_OK){
                val data  = resultado .data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val cuenta  = task.getResult(ApiException::class.java)
                    autenticacionGoogle(cuenta.idToken)
                }catch (e:Exception){
                    Toast.makeText(this,
                        "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }

        private fun autenticacionGoogle(idToken: String?) {
            val credential  = GoogleAuthProvider.getCredential(idToken,null)
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {resultadoAuth->
                    if(resultadoAuth.additionalUserInfo!!.isNewUser){
                        llenarInfoBD()
                    }else{
                        startActivity(Intent(this,MainActivity::class.java))
                        finishAffinity()
                    }
                }
                .addOnFailureListener{e->
                    Toast.makeText(this,
                        "${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        private fun llenarInfoBD() {
            progressDialog.setMessage("Guardando Informacion")

            val tiempo = Constantes.obtenerTiempoDis()
            val emailUsuario = firebaseAuth.currentUser!!.email
            val uidUsuario = firebaseAuth.uid
            val nombreUsuario = firebaseAuth.currentUser?.displayName

            val hashMap = HashMap<String, Any>()
            hashMap["nombres"] = "${nombreUsuario}"
            hashMap["codigoTelefono"] = ""
            hashMap["telefono"] = ""
            hashMap["urlImagenPerfil"] = ""
            hashMap["proveedor"] = "Google"
            hashMap["escribiendo"] = ""
            hashMap["tiempo"] = tiempo
            hashMap["estado"] = "online"
            hashMap["email"] = "${emailUsuario}"
            hashMap["uid"] = "${uidUsuario}"
            hashMap["fecha_nac"] = ""

            val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
            ref.child(uidUsuario!!)
                .setValue(hashMap)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    startActivity(Intent(this,MainActivity::class.java))
                    finishAffinity()
                }
                .addOnFailureListener {e->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this,
                        "No se registro debido a ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
        }


        private fun comprobarSesion(){
            if(firebaseAuth.currentUser !=null){
                startActivity(Intent(this,MainActivity::class.java))
                finishAffinity()
            }
        }
    }