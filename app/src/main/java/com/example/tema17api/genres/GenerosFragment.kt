package com.example.tema17api.genres

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tema17api.models.GenresResponse
import com.example.tema17api.movies.MovieFragment
import com.example.tema17api.R
import com.example.tema17api.api.ApiRest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenerosFragment : Fragment() {

    private lateinit var rvGeneros: RecyclerView
    val TAG = "MainActivity"
    var data: ArrayList<GenresResponse.Genre> = ArrayList()
    private var adapter: GenresAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_name)
        rvGeneros = view.findViewById<RecyclerView>(R.id.rvGeneros)
        //Mostrar como cuadricula
        val mLayoutManager = GridLayoutManager(context, 2)
        rvGeneros.layoutManager = mLayoutManager
        adapter = GenresAdapter(data) {genre ->
            activity?.let {
                val fragment = MovieFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable("genre", genre)
                }
                it.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).addToBackStack(null).commit()
            }
        }
        rvGeneros.adapter = adapter


        ApiRest.initService()
        getGenres()
    }
    private fun getGenres() {
        val call = ApiRest.service.getGenres()
        call.enqueue(object : Callback<GenresResponse> {
            override fun onResponse(call: Call<GenresResponse>, response: Response<GenresResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.genres)
                    adapter?.notifyDataSetChanged()
                    Log.d(TAG,data.toString())
// Imprimir aqui el listado con logs
                } else {
                    Log.e(TAG, response.errorBody()?.string()?:"Error")
                }
            }
            override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }
}