package com.example.truequesperu

import android.content.Intent
import android.icu.text.DateFormat
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.Arrays
import java.util.Calendar
import java.util.Locale

object Constantes {

    const val MENSAJE_TIPO_TEXTO = "TEXTO"
    const val MENSAJE_TIPO_IMAGEN = "IMAGEN"

    const val anuncio_disponible = "Disponible"
    const val anuncio_vendido = "Vendido"

    const val NOTIFICACION_DE_NUEVO_MENSAJE = "NOTIFICACION_DE_NUEVO_MENSAJE"
    const val FCM_SERVER_KEY =
        "AAAAhKPmP18:APA91bFCI0BIeCMGCXHy1mYCmVpwgZo1at0wrlRUQhhARfQaMkoczZ1GL1R8QLlZzhDLexnQ3Me_Xdwe6iYXnqOmZPfit90Fj1HBwSsbfJGB59NZ6HPCLW3JCC63wIWWdsfxqEA2PW9p"

    val categorias = arrayOf(
        "Todos",
        "Móbiles",
        "Ordenadores/Laptops",
        "Electrónica y electrodomésticos",
        "Vehículos",
        "Consolas y Videojuegos",
        "Hogar y Muebles",
        "Belleza y cuidado personal",
        "Libros",
        "Deportes",
        "Juguetes y figuras",
        "Mascotas",
        "Otros"
    )

    /*val categoriasIcono = arrayOf(
        R.drawable.iconcategoriatodos,
        R.drawable.iconcategoriamobiles,
        R.drawable.iconcategoriaordenadores,
        R.drawable.iconcategoriaelectrodomesticos,
        R.drawable.iconcategoriavehiculos,
        R.drawable.iconcategoriacosola,
        R.drawable.iconcategoriamuebles,
        R.drawable.iconcategoriabelleza,
        R.drawable.iconcategorialibros,
        R.drawable.iconcategoriadeportes,
        R.drawable.iconcategoriajuguetes,
        R.drawable.iconcategoriamascotas,
        R.drawable.iccategoriaotros
    )*/

    val condiciones = arrayOf(
        "Nuevo",
        "Usado",
        "Renovado/Restaurado"
    )

    fun obtenerTiempoDis(): Long {
        return System.currentTimeMillis()
    }

    fun obtenerFecha(tiempo : Long) :String {
        val calendario = Calendar.getInstance(Locale.ENGLISH)
        calendario.timeInMillis = tiempo

        return DateFormat.format("dd/MM/yyyy", calendario).toString()
    }

    fun obtenerFechaHora(tiempo: Long) : String {
        val calendario = Calendar.getInstance(Locale.ENGLISH)
        calendario.timeInMillis = tiempo
        return DateFormat.format("dd/MM/yyyy hh:mm:a", calendario).toString()
    }

    fun agregarAnuncioFav(context : Context, idAnuncio : String){
        val firebaseAuth = FirebaseAuth.getInstance()
        val tiempo = Constantes.obtenerTiempoDis()

        val hashMap = HashMap<String,Any>()
        hashMap["idAnuncio"] = idAnuncio
        hashMap["tiempo"] = tiempo

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idAnuncio)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context,
                    "Anuncio agregado a favoritos",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context,
                    "${e.message}",
                    Toast.LENGTH_SHORT).show()

            }
    }

    fun eliminarAnuncioFav(context: Context, idAnuncio: String){
        val firebaseAuth = FirebaseAuth.getInstance()

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idAnuncio)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,
                    "Anuncio eliminado de favoritos",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context,
                    "${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    fun mapaIntent(context : Context , latitud : Double, longitud : Double){
        val googleMapaIntentUri = Uri.parse("http://maps.google.com/maps?daddr=$latitud,$longitud")

        val mapIntent = Intent(Intent.ACTION_VIEW, googleMapaIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if(mapIntent.resolveActivity(context.packageManager)!=null){
            //La app de google maps si esta instalada
            context.startActivity(mapIntent)
        }else{
            //Si la app de google maps no esta instalada
            Toast.makeText(context,"No tienes instalada la aplicacion de Google Maps",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun llamarIntent(context : Context, tef : String){
        val intent = Intent(Intent.ACTION_CALL)
        intent .setData(Uri.parse("tel:${tef}"))
        context.startActivity(intent)
    }

    fun smsIntent(context : Context , tel : String){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("smsto:${tel}"))
        intent.putExtra("sms_body","")
        context.startActivity(intent)
    }

    fun rutaChat(receptorUid : String, emisorUid : String) :String{
        val arrayUid = arrayOf(receptorUid,emisorUid)
        Arrays.sort(arrayUid)
        return "${arrayUid[0]}_${arrayUid[1]}"
    }

    fun incrementarVistas(idAnuncio: String){
        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        ref.child(idAnuncio)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var vistasActuales = "${snapshot.child("contadorVistas").value}"
                    if(vistasActuales == "" || vistasActuales == "null"){
                        vistasActuales = "0"
                    }

                    val nuevaVista = vistasActuales.toLong()+1

                    val hashMap = HashMap<String, Any>()
                    hashMap["contadorVistas"] = nuevaVista

                    val bdRef = FirebaseDatabase.getInstance().getReference("Anuncios")
                    bdRef.child(idAnuncio)
                        .updateChildren(hashMap)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}