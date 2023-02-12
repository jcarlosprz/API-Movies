package com.example.tema17api.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tema17api.models.MoviesResponse
import com.example.tema17api.R
import com.example.tema17api.api.ApiRest
import com.squareup.picasso.Picasso

class MoviesAdapter(private val data: ArrayList<MoviesResponse.Movies>, val onCLick:(MoviesResponse.Movies)-> Unit) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo = itemView.findViewById<TextView>(R.id.titulo)
        val img = itemView.findViewById<ImageView>(R.id.imgVV)
        val card = itemView.findViewById<CardView>(R.id.card)
        fun bind(item: MoviesResponse.Movies) {
            titulo.text = item.title
            Picasso.get().load("${ApiRest.URL_IMAGES}${item.posterPath}").into(img)
            card.setCardBackgroundColor(generateColor().toInt())
            card.setOnClickListener {
                onCLick(item)
            }
        }

        fun generateColor(): Long {
            val colors = arrayListOf(
                0xfff44336, 0xffe91e63, 0xff9c27b0, 0xff673ab7,
                0xff3f51b5, 0xff2196f3, 0xff03a9f4, 0xff00bcd4,
                0xff009688, 0xff4caf50, 0xff8bc34a, 0xffcddc39,
                0xffffeb3b, 0xffffc107, 0xffff9800, 0xffff5722,
                0xff795548, 0xff9e9e9e, 0xff607d8b, 0xff333333
            )
            return colors.random()
        }
    }
}