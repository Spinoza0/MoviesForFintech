package com.spinoza.moviesforfintech.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.spinoza.moviesforfintech.domain.model.Film

class FilmDiffCallback : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem.filmId == newItem.filmId
    }

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem == newItem
    }
}