package com.spinoza.moviesforfintech.presentation.adapter

import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.spinoza.moviesforfintech.databinding.FilmItemBinding
import com.spinoza.moviesforfintech.domain.model.Film
import javax.inject.Inject

class FilmsListAdapter @Inject constructor() :
    ListAdapter<Film, FilmViewHolder>(FilmDiffCallback()) {

    var onFilmItemLongClickListener: ((Film) -> Unit)? = null
    var onFilmItemClickListener: ((Film) -> Unit)? = null
    var onReachEndListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = FilmItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = getItem(position)
        with(holder.binding) {
            Glide.with(root).load(film.posterUrlPreview).into(imageViewPoster)
            textViewName.text = film.nameRu
            textGenreYear.text = String.format("%s (%s)", film.genres, film.year)
            when (film.isFavourite) {
                true -> imageViewStar.visibility = VISIBLE
                false -> imageViewStar.visibility = INVISIBLE
            }
            root.setOnClickListener { onFilmItemClickListener?.invoke(film) }
            root.setOnLongClickListener {
                onFilmItemLongClickListener?.invoke(film)
                true
            }
            if (position >= itemCount - 10) {
                onReachEndListener?.invoke()
            }
        }
    }

    override fun onViewRecycled(holder: FilmViewHolder) {
        super.onViewRecycled(holder)
        with(holder.binding) {
            imageViewPoster.setImageDrawable(null)
            textViewName.text = null
            textGenreYear.text = null
        }
    }
}