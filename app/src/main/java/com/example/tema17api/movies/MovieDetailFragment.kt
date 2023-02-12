package com.example.tema17api.movies

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tema17api.R
import com.example.tema17api.api.ApiRest
import com.example.tema17api.models.GenreMovieDetailResponse
import com.example.tema17api.models.MoviesResponse
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailFragment : Fragment() {
    val TAG = "MainActivity"
    var genreData: List<GenreMovieDetailResponse.Genre> = emptyList()
    lateinit var chipGroup: ChipGroup
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)
        val ivBanner = view.findViewById<ImageView>(R.id.ivBanner)
        val ivPoster = view.findViewById<ImageView>(R.id.ivPoster)
        val fechaEstreno = view.findViewById<TextView>(R.id.tvEstreno)
        val valoracion = view.findViewById<TextView>(R.id.tvValoracion)
        val descripcion = view.findViewById<TextView>(R.id.tvDescripcion)

        val movie =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable("movie", MoviesResponse.Movies::class.java)
            } else {
                arguments?.getSerializable("movie") as? MoviesResponse.Movies
            }
        (activity as? AppCompatActivity)?.supportActionBar?.title = movie?.title

        getGenreMovie(movie?.id.toString())
        Log.i("MainActivity", movie?.id.toString())

        Picasso.get().load("${ApiRest.URL_IMAGES}${movie?.backdropPath}").into(ivBanner)
        Picasso.get().load("${ApiRest.URL_IMAGES}${movie?.posterPath}").into(ivPoster)

        val año = movie?.releaseDate.toString()
        val shortenedText = if (año.length > 4) año.substring(0, 4) else año
        fechaEstreno.text = shortenedText
        valoracion.text = movie?.voteAverage.toString()
        if (movie?.overview.toString()==""){
            descripcion.text = "Sin descripción"
        }else{
            descripcion.text = movie?.overview.toString()
        }

    }

    private fun addChip(a: String) {
        val genre = Chip(context)
        genre.text = a
        genre.setChipStrokeColorResource(R.color.yellow)
        genre.setChipBackgroundColorResource(R.color.yellow)
        genre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        genre.setTextColor(resources.getColor(R.color.black))
        genre.setChipStrokeWidthResource(R.dimen.chip_stroke_width)
        chipGroup.addView(genre)
    }

    private fun getGenreMovie(a: String) {
        val call = ApiRest.service.getGenreMovie(a)
        call.enqueue(object : Callback<GenreMovieDetailResponse> {
            override fun onResponse(
                call: Call<GenreMovieDetailResponse>,
                response: Response<GenreMovieDetailResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    genreData = body.genres

                    var myStr = ""
                    for (elt in genreData) {
                        myStr += elt.name + "  "
                    }
                    body.genres.forEach {
                        addChip(it.name)
                    }

                    Log.i(TAG, genreData.toString())

                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<GenreMovieDetailResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }


}
