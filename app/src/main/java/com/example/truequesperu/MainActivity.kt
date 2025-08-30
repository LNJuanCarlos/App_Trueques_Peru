package com.example.truequesperu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.truequesperu.Fragmentos.FragmentChats
import com.example.truequesperu.Fragmentos.FragmentCuenta
import com.example.truequesperu.Fragmentos.FragmentInicio
import com.example.truequesperu.Fragmentos.FragmentMisAnuncios
import com.example.truequesperu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        verFragmentInicio()
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets*/

            binding.BottomNV.setOnItemSelectedListener {item->
                when(item.itemId){
                    R.id.Item_Inicio->{
                        verFragmentInicio()
                        true
                    }

                    R.id.Item_Chats->{
                        verFragmentChats()
                        true
                    }

                    R.id.Item_Mis_Anuncios->{
                        verFragmentMisAnuncios()
                        true
                    }

                    R.id.Item_Cuenta->{
                        verFragmentCuenta()
                        true
                    }
                    else->{
                        false
                    }
                }
            }

        }

    private fun verFragmentInicio(){
        binding.TituloRl.text = "Inicio"
        val fragment = FragmentInicio()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentL1.id, fragment, "FragmentInicio")
        fragmentTransaction.commit()

    }

    private fun verFragmentChats(){
        binding.TituloRl.text = "Chats"
        val fragment = FragmentChats()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentL1.id, fragment, "FragmentChats")
        fragmentTransaction.commit()

    }

    private fun verFragmentMisAnuncios(){
        binding.TituloRl.text = "MisAnuncios"
        val fragment = FragmentMisAnuncios()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentL1.id, fragment, "FragmentMisAnuncios")
        fragmentTransaction.commit()

    }

    private fun verFragmentCuenta(){
        binding.TituloRl.text = "Cuenta"
        val fragment = FragmentCuenta()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentL1.id, fragment, "FragmentCuenta")
        fragmentTransaction.commit()

    }

    }
