package com.example.webservicedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webservicedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter:DogAdapter
    private val dogImages = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        //binding.svDogs.setOnQueryTextListener(this)
        findViewById<SearchView>(R.id.svDogs).setOnQueryTextListener(this)
        initRecyclerView()
        searchByName("akita")
    }
    private fun initRecyclerView(){
        adapter = DogAdapter(dogImages)
        findViewById<RecyclerView>(R.id.rvDogs).layoutManager=LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.rvDogs).adapter=adapter
        //binding.rvDogs.layoutManager=LinearLayoutManager(this)
        // binding.rvDogs.adapter = adapter
    }
    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun searchByName(query:String){
        Toast.makeText(this,"buscando",Toast.LENGTH_LONG).show()
        CoroutineScope(Dispatchers.IO).launch {
            val call:Response<DogsResponse> = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppies:DogsResponse? = call.body()
            runOnUiThread {
                if(call.isSuccessful){
                    //Mostrar recycler
                    val images:List<String> = puppies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()
                }else{
                    //mostrar error
                    showError()
                }

            }
        }
    }
    private fun showError(){
        Toast.makeText(this,"Ha ocurrido un error",Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(this,"buscando",Toast.LENGTH_LONG).show()
        if(!query.isNullOrEmpty()){
            searchByName(query.toLowerCase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}