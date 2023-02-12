package com.example.tema17api.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tema17api.R
import com.example.tema17api.api.ApiRest
import com.example.tema17api.models.GenresResponse
import com.example.tema17api.models.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieFragment : Fragment() {
    private lateinit var rvMovies: RecyclerView
    val TAG = "MainActivity"
    var genreMovies: ArrayList<MoviesResponse.Movies> = ArrayList()
    private var adapter: MoviesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genre =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable("genre", GenresResponse.Genre::class.java)
            } else {
                arguments?.getSerializable("genre") as? GenresResponse.Genre
            }
        (activity as? AppCompatActivity)?.supportActionBar?.title = genre?.name
        rvMovies = view.findViewById<RecyclerView>(R.id.rvMovies)
        //Mostrar como cuadricula
        val mLayoutManager = GridLayoutManager(context, 2)
        rvMovies.layoutManager = mLayoutManager

        adapter = MoviesAdapter(genreMovies) { movie ->
            activity?.let {
                val fragment = MovieDetailFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable("movie", movie)
                }
                it.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).addToBackStack(null).commit()
            }
        }

        rvMovies.adapter = adapter

        getMovies((genre?.id).toString())
    }

    private fun getMovies(a: String) {
        val call = ApiRest.service.getMovie(a)
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    genreMovies.clear()
                    genreMovies.addAll(body.results)
                    adapter?.notifyDataSetChanged()
                    Log.d(TAG, genreMovies.toString())
                    // Imprimir aqui el listado con logs
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }
}

